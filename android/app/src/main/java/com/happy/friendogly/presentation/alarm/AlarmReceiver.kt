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
import com.happy.friendogly.domain.usecase.GetWoofAlarmUseCase
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
    lateinit var getWoofAlarmUseCase: GetWoofAlarmUseCase

    @Inject
    lateinit var getChatAlarmUseCase: GetChatAlarmUseCase

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data[ALARM_TYPE] == "CHAT") {
            showChatAlarm(
                message,
            )
        } else if (message.data[ALARM_TYPE] == "FOOTPRINT") {
            showWoofAlarm(message.data[ALARM_TITLE], message.data[ALARM_BODY])
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showChatAlarm(message: RemoteMessage) =
        CoroutineScope(Dispatchers.IO).launch {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (getChatAlarmUseCase()
                    .getOrDefault(true) && ChatLifecycleObserver.getInstance().isBackground
            ) {
                createNotificationChannel(AlarmType.CHAT)
                showChatNotification(
                    chatRoomId = message.data["chatRoomId"]?.toLongOrNull(),
                    chatRoomName = message.data["chatRoomName"],
                    chatRoomImage = message.data["chatRoomImage"],
                    senderName = message.data["senderName"],
                    messageContent = message.data["content"],
                    senderProfile = message.data["profilePictureUrl"],
                )
            }
        }

    private fun showWoofAlarm(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (getWoofAlarmUseCase.invoke().getOrDefault(true)) {
            createNotificationChannel(AlarmType.WOOF)
            deliverWoofNotification(title, body)
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
        chatRoomId: Long?,
        chatRoomName: String?,
        chatRoomImage: String?,
        senderName: String?,
        messageContent: String?,
        senderProfile: String?,
    ) {
        val contentChatIntent =
            if (chatRoomId == null) {
                MainActivity.getIntent(this)
            } else {
                ChatActivity.getIntent(this, chatRoomId)
            }

        val contentChatPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                contentChatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )

        val person =
            createPerson(senderName, senderProfile)

        val messagingStyle =
            NotificationCompat.MessagingStyle(person)
                .setGroupConversation(true)
                .addMessage(
                    messageContent ?: "",
                    System.currentTimeMillis(),
                    person,
                )

        pushShortCutInfo(chatRoomId, contentChatIntent, chatRoomName, chatRoomImage)

        val notification =
            createNotification(
                chatRoomName,
                messageContent,
                messagingStyle,
                contentChatPendingIntent,
                chatRoomId,
            )

        val groupNotification = createGroupNotifyNotification(chatRoomName, messagingStyle, chatRoomId)
        notificationManager.apply {
            notify(GROUP_KEY.hashCode(), groupNotification)
            notify(chatRoomId?.toInt() ?: INVALID_CHAT_ROOM_ID, notification)
        }
    }

    private fun createPerson(
        senderName: String?,
        senderProfile: String?,
    ) = Person.Builder()
        .setName(senderName)
        .setIcon(IconCompat.createWithBitmap(createRoundedBitmap(senderProfile!!)))
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
        chatRoomName: String?,
        messagingStyle: NotificationCompat.MessagingStyle,
        chatRoomId: Long?,
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
            .setShortcutId(chatRoomId?.toString())
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()
    }

    private fun pushShortCutInfo(
        chatRoomId: Long?,
        contentIntent: Intent,
        chatRoomName: String?,
        chatRoomImage: String?,
    ) {
        val shortCutInfo =
            ShortcutInfoCompat.Builder(this@AlarmReceiver, chatRoomId.toString())
                .setIntent(contentIntent).setShortLabel(chatRoomName!!)
                .setIcon(
                    IconCompat.createWithBitmap(
                        createRoundedBitmap(
                            chatRoomImage!!,
                        ),
                    ),
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

        return getRoundedCornerBitmap(bitmap, cornerRadius = IMAGE_CORNER_ROUND)
    }

    private fun getRoundedCornerBitmap(
        bitmap: Bitmap,
        cornerRadius: Float,
    ): Bitmap {
        val round = RoundedBitmapDrawableFactory.create(resources, bitmap)

        round.cornerRadius = cornerRadius
        round.setAntiAlias(true)
        return round.toBitmap()
    }

    private fun deliverWoofNotification(
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
            NotificationCompat.Builder(this, WOOF_CHANNEL_ID)
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
        WOOF,
        ;

        fun channelId(): String =
            when (this) {
                CHAT -> CHAT_CHANNEL_ID
                WOOF -> WOOF_CHANNEL_ID
            }

        fun channelName(): String =
            when (this) {
                CHAT -> CHAT_CHANNEL_NAME
                WOOF -> WOOF_CHANNEL_NAME
            }
    }

    companion object {
        private const val CHAT_CHANNEL_ID = "chat_channel"
        private const val CHAT_CHANNEL_NAME = "채팅"
        private const val WOOF_CHANNEL_ID = "woof_channel"
        private const val WOOF_CHANNEL_NAME = "친구 찾기"
        private const val ALARM_TITLE = "title"
        private const val ALARM_BODY = "body"
        private const val ALARM_TYPE = "type"
        private const val INVALID_CHAT_ROOM_ID = -1
        private const val GROUP_KEY = "chat"

        private const val REQUEST_CODE = 0

        private const val IMAGE_CORNER_ROUND = 70f

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
