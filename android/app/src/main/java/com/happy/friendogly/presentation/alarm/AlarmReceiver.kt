package com.happy.friendogly.presentation.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.happy.friendogly.R
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundAlarmUseCase
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.MainActivity.Companion.EXTRA_FRAGMENT
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatLifecycleObserver
import com.happy.friendogly.presentation.ui.playground.PlaygroundFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var getPlaygroundAlarmUseCase: GetPlaygroundAlarmUseCase

    @Inject
    lateinit var getChatAlarmUseCase: GetChatAlarmUseCase

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data[ALARM_TYPE] == "CHAT") {
            showChatAlarm(
                message,
            )
        } else if (message.data[ALARM_TYPE] == "PLAYGROUND") {
            showPlaygroundAlarm(message.data[ALARM_TITLE], message.data[ALARM_BODY])
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showChatAlarm(message: RemoteMessage) =
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (getChatAlarmUseCase()
                    .getOrDefault(true) && ChatLifecycleObserver.getInstance().isBackground
            ) {
                createNotificationChannel(AlarmType.CHAT)
                showChatNotification(
                    chatRoomId = message.data.getValue("chatRoomId").toLong(),
                    chatRoomName = message.data.getValue("clubTitle"),
                    chatRoomImage = message.data["clubPictureUrl"],
                    senderName = message.data.getValue("senderName"),
                    messageContent = message.data.getValue("content"),
                    senderProfile = message.data["profilePictureUrl"],
                )
            }
        }

    private fun Map<String, String>.getValue(key: String): String =
        this[key] ?: throw IllegalArgumentException("채팅 알림 데이터에 $key 에 관한 값이 없습니다.")

    private fun showPlaygroundAlarm(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (getPlaygroundAlarmUseCase().getOrDefault(true)) {
            createNotificationChannel(AlarmType.PLAYGROUND)
            deliverPlaygroundNotification(title, body)
        }
    }

    private fun createNotificationChannel(type: AlarmType) {
        val notificationChannel =
            NotificationChannel(
                type.channelId(),
                type.channelName(),
                NotificationManager.IMPORTANCE_HIGH,
            )
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationManager.createNotificationChannel(
            notificationChannel,
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showChatNotification(
        chatRoomId: Long,
        chatRoomName: String,
        chatRoomImage: String?,
        senderName: String,
        messageContent: String,
        senderProfile: String?,
    ) {
        val contentChatPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                ChatActivity.getIntent(this, chatRoomId),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )

        val person =
            createPerson(senderName, senderProfile)

        val messagingStyle =
            NotificationCompat.MessagingStyle(person)
                .setGroupConversation(true)
                .addMessage(
                    messageContent,
                    System.currentTimeMillis(),
                    person,
                )

        pushShortCutInfo(
            chatRoomId,
            ChatActivity.getIntent(this, chatRoomId),
            chatRoomName,
            chatRoomImage,
        )

        val notification =
            createNotification(
                chatRoomName,
                messageContent,
                messagingStyle,
                contentChatPendingIntent,
                chatRoomId,
            )

        val groupNotification =
            createGroupNotifyNotification(chatRoomName, messagingStyle, chatRoomId)
        notificationManager.apply {
            notify(GROUP_KEY.hashCode(), groupNotification)
            notify(chatRoomId.toInt(), notification)
        }
    }

    private fun createPerson(
        senderName: String,
        senderProfile: String?,
    ) = Person.Builder()
        .setName(senderName)
        .setIcon(
            if (senderProfile.isNullOrBlank()) {
                IconCompat.createWithResource(this, R.drawable.ic_normal_profile)
            } else {
                IconCompat.createWithBitmap(createRoundedBitmap(senderProfile))
            },
        )
        .build()

    private fun createNotification(
        chatRoomName: String?,
        messageContent: String?,
        messagingStyle: NotificationCompat.MessagingStyle,
        contentPendingIntent: PendingIntent?,
        chatRoomId: Long?,
    ): Notification {
        return NotificationCompat.Builder(this@AlarmReceiver, CHAT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(chatRoomName)
            .setContentText(messageContent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(
                messagingStyle,
            )
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .setShortcutId(chatRoomId?.toString())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createGroupNotifyNotification(
        chatRoomName: String,
        messagingStyle: NotificationCompat.MessagingStyle,
        chatRoomId: Long,
    ): Notification {
        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                REQUEST_CODE,
                MainActivity.getIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )

        return NotificationCompat.Builder(this@AlarmReceiver, CHAT_CHANNEL_ID)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setStyle(messagingStyle.setConversationTitle(chatRoomName))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(contentPendingIntent)
            .setShortcutId(chatRoomId.toString())
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()
    }

    private fun pushShortCutInfo(
        chatRoomId: Long,
        contentIntent: Intent,
        chatRoomName: String,
        chatRoomImage: String?,
    ) {
        val shortCutInfo =
            ShortcutInfoCompat.Builder(this@AlarmReceiver, chatRoomId.toString())
                .setIntent(contentIntent).setShortLabel(chatRoomName)
                .setIcon(
                    if (chatRoomImage.isNullOrBlank()) {
                        IconCompat.createWithResource(this, R.mipmap.ic_launcher)
                    } else {
                        IconCompat.createWithBitmap(
                            createRoundedBitmap(
                                chatRoomImage,
                            ),
                        )
                    },
                )
                .setLongLabel(chatRoomName)
                .setAlwaysBadged()
                .setIntent(
                    contentIntent.setAction(
                        Intent.ACTION_MAIN,
                    ),
                )
                .setAlwaysBadged()
                .setLongLived(true).build()

        ShortcutManagerCompat.pushDynamicShortcut(
            this@AlarmReceiver,
            shortCutInfo,
        )
    }

    private fun createRoundedBitmap(imageUrl: String): Bitmap {
        val url = URL(imageUrl)
        val bitmap = BitmapFactory.decodeStream(url.openStream())

        return getRoundedCornerBitmap(bitmap)
    }

    private fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
        val round = RoundedBitmapDrawableFactory.create(resources, bitmap)

        round.isCircular = true
        round.setAntiAlias(true)
        return round.toBitmap()
    }

    private fun deliverPlaygroundNotification(
        title: String?,
        body: String?,
    ) {
        val contentIntent =
            MainActivity.getIntent(this).apply {
                putExtra(EXTRA_FRAGMENT, PlaygroundFragment.TAG)
            }

        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                DEFAULT_INTENT_ID,
                contentIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat.Builder(this, PLAYGROUND_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    enum class AlarmType {
        CHAT,
        PLAYGROUND,
        ;

        fun channelId(): String =
            when (this) {
                CHAT -> CHAT_CHANNEL_ID
                PLAYGROUND -> PLAYGROUND_CHANNEL_ID
            }

        fun channelName(): String =
            when (this) {
                CHAT -> CHAT_CHANNEL_NAME
                PLAYGROUND -> PLAYGROUND_CHANNEL_NAME
            }
    }

    companion object {
        private const val CHAT_CHANNEL_ID = "chat_channel"
        private const val CHAT_CHANNEL_NAME = "채팅"
        private const val PLAYGROUND_CHANNEL_ID = "playground_channel"
        private const val PLAYGROUND_CHANNEL_NAME = "놀이터"
        private const val ALARM_TITLE = "title"
        private const val ALARM_BODY = "body"
        private const val ALARM_TYPE = "type"
        private const val GROUP_KEY = "chat"

        private const val REQUEST_CODE = 0

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
