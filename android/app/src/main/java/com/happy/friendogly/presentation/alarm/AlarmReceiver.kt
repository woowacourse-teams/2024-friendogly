package com.happy.friendogly.presentation.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.permission.AlarmPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (token.isNotBlank()){
                AppModule.getInstance().saveAlarmTokenUseCase.invoke(token)
            }
        }
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            onReceive(message.notification?.title, message.notification?.body)
        } else if (message.data.isNotEmpty()) {
            onReceive(message.data[ALARM_TITLE], message.data[ALARM_BODY])
        }
    }

    private fun onReceive(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (AppModule.getInstance().getAlarmSettingUseCase.invoke().getOrDefault(false)) {
            createNotificationChannel()
            deliverNotification(title, body)
        }
    }

    private fun createNotificationChannel() {
        if (AlarmPermission.isValidPermissionSDK()) {
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
    }

    private fun deliverNotification(
        title: String?,
        body: String?,
    ) {
        val contentIntent = MainActivity.getIntent(this)
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

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
