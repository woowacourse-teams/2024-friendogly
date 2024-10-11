package com.happy.friendogly.presentation.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.happy.friendogly.R
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetWoofAlarmUseCase
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.MainActivity.Companion.EXTRA_FRAGMENT
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatLifecycleObserver
import com.happy.friendogly.presentation.ui.woof.WoofFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
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
                message.data["senderName"],
                message.data["content"],
                message.data["chatRoomId"]?.toLongOrNull(),
            )
        } else if (message.data[ALARM_TYPE] == "FOOTPRINT") {
            showWoofAlarm(message.data[ALARM_TITLE], message.data[ALARM_BODY])
        }
    }

    private fun showChatAlarm(
        title: String?,
        body: String?,
        chatRoomId: Long?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (getChatAlarmUseCase()
                .getOrDefault(true) && ChatLifecycleObserver.getInstance().isBackground
        ) {
            createNotificationChannel()
            deliverChatNotification(title, body, chatRoomId)
        }
    }

    private fun showWoofAlarm(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (getWoofAlarmUseCase.invoke().getOrDefault(true)) {
            createNotificationChannel()
            deliverWoofNotification(title, body)
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(
            notificationChannel,
        )
    }

    private fun deliverChatNotification(
        title: String?,
        body: String?,
        chatRoomId: Long?,
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
                PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationManager.notify(chatRoomId?.toInt() ?: INVALID_CHAT_ROOM_ID, builder.build())
    }

    private fun deliverWoofNotification(
        title: String?,
        body: String?,
    ) {
        val contentIntent =
            MainActivity.getIntent(this).apply {
                putExtra(EXTRA_FRAGMENT, WoofFragment.TAG)
            }

        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                DEFAULT_INTENT_ID,
                contentIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "alarm_id"
        private const val CHANNEL_NAME = "alam"
        private const val ALARM_TITLE = "title"
        private const val ALARM_BODY = "body"
        private const val ALARM_TYPE = "type"
        private const val INVALID_CHAT_ROOM_ID = -1

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
