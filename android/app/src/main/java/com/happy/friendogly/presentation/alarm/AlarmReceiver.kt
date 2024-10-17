package com.happy.friendogly.presentation.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class AlarmReceiver : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var getWoofAlarmUseCase: GetWoofAlarmUseCase

    @Inject
    lateinit var getChatAlarmUseCase: GetChatAlarmUseCase

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
                showBubbleNotification(
                    chatRoomId = message.data["chatRoomId"]?.toLongOrNull(),
                    chatRoomName = message.data["chatRoomName"],
                    senderName = message.data["senderName"],
                    messageContent = message.data["content"],
                    senderProfile = message.data["profilePictureUrl"],
                    isGroupChat = true,
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
        notificationManager.createNotificationChannel(
            notificationChannel,
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun showBubbleNotification(
        chatRoomId: Long?,
        chatRoomName: String?,
        senderName: String?,
        messageContent: String?,
        senderProfile: String?,
        isGroupChat: Boolean,
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "chat_bubble_channel",
                    "Chat Bubbles",
                    NotificationManager.IMPORTANCE_HIGH,
                )
            notificationManager.createNotificationChannel(channel)
        }

        val contentIntent =
            if (chatRoomId == null) {
                MainActivity.getIntent(this)
            } else {
                ChatActivity.getIntent(this, chatRoomId)
            }

        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )

        // Glide를 사용하여 프로필 이미지 로드 후 아이콘 설정
        Glide.with(this)
            .asBitmap()
            .load(senderProfile)
            .transform(RoundedCorners(30))
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        val person =
                            Person.Builder()
                                .setName(senderName)
                                .setIcon(IconCompat.createWithBitmap(resource))
                                .build()

                        val messagingStyle =
                            NotificationCompat.MessagingStyle(person)
                                .setGroupConversation(true)
                                .addMessage(
                                    messageContent ?: "",
                                    System.currentTimeMillis(),
                                    person
                                )


                        val notificationTitle =
                            if (isGroupChat && !chatRoomName.isNullOrEmpty()) {
                                chatRoomName
                            } else {
                                senderName
                            }

                        val notification =
                            NotificationCompat.Builder(this@AlarmReceiver, CHAT_CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(notificationTitle)
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

                        val groupNotification =
                            NotificationCompat.Builder(this@AlarmReceiver, CHAT_CHANNEL_ID)
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

                        ShortcutManagerCompat.pushDynamicShortcut(
                            this@AlarmReceiver,
                            ShortcutInfoCompat.Builder(this@AlarmReceiver, chatRoomId.toString())
                                .setIntent(contentIntent).setShortLabel(chatRoomName!!)
                                .setLongLabel(chatRoomName)
                                .setIntent(
                                    contentIntent.setAction(
                                        Intent.ACTION_MAIN,
                                    ),
                                )
                                .setLongLived(true).setPerson(person).build(),
                        )

                        notificationManager.apply {
                            notify(GROUP_KEY.hashCode(), groupNotification)
                            notify(chatRoomId?.toInt() ?: INVALID_CHAT_ROOM_ID, notification)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                },
            )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun deliverChatNotification(
        chatRoomId: Long?,
        chatRoomImage: String?,
        chatRoomName: String?,
        senderName: String?,
        messageContent: String?,
        senderProfile: String?,
    ) {
        val contentIntent =
            if (chatRoomId == null) {
                MainActivity.getIntent(this)
            } else {
                ChatActivity.getIntent(this, chatRoomId)
            }

        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                DEFAULT_INTENT_ID,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )

        val person = Person.Builder().setName(senderName).build()

//        Glide.with(this)
//            .asBitmap()
//            .load(senderProfile)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: Transition<in Bitmap>?
//                ) {
//                    person.setIcon(IconCompat.createWithBitmap(resource))
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//
//            })

        val message =
            NotificationCompat.MessagingStyle.Message(
                messageContent,
                System.currentTimeMillis(),
                person,
            )

        val bubbleData =
            NotificationCompat.BubbleMetadata.Builder(chatRoomId.toString())
                .setAutoExpandBubble(true)
                .setSuppressNotification(true)
                .build()

        val builder =
            NotificationCompat.Builder(this, CHAT_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(chatRoomName)
                .setContentText(messageContent)
                .setBubbleMetadata(bubbleData)
                .setGroup(GROUP_KEY)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.MessagingStyle(person).addMessage(message),
                )
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        val groupNotification =
            NotificationCompat.Builder(this, CHAT_CHANNEL_ID)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(NotificationCompat.InboxStyle())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentPendingIntent)
                .setContentText("새로운 알림이 왔습니다.")
                .setShortcutId(chatRoomId?.toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        ShortcutManagerCompat.pushDynamicShortcut(
            this,
            ShortcutInfoCompat.Builder(this, chatRoomId.toString())
                .setIntent(contentIntent).setShortLabel(chatRoomName!!).setLongLabel(chatRoomName)
                .setIntent(
                    contentIntent.setAction(
                        Intent.ACTION_MAIN,
                    ),
                )
                .setLongLived(true).setPerson(person).build(),
        )

        notificationManager.apply {
            notify(chatRoomId?.toInt() ?: INVALID_CHAT_ROOM_ID, builder.build())
            notify(GROUP_KEY, chatRoomId!!.toInt(), groupNotification.build())
        }
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

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
