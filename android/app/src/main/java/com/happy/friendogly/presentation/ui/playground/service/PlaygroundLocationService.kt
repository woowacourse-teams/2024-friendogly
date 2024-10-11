package com.happy.friendogly.presentation.ui.playground.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.MainActivity.Companion.EXTRA_FRAGMENT
import com.happy.friendogly.presentation.ui.playground.PlaygroundFragment
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationReceiver.Companion.ACTION_LOCATION_UPDATE

class PlaygroundLocationService : Service() {
    private lateinit var locationManager: PlaygroundLocationManager
    private var currentLocation: Location? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        val playStatus = intent?.getSerializableExtra(EXTRA_PLAY_STATUS) as PlayStatus
        val playStatusTitle = convertPlayStatusToTitle(playStatus)

        startForegroundService(playStatusTitle)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.stopLocationUpdate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService(playStatusTitle: String) {
        startLocationUpdate()
        createNotificationChannel()
        startForeground(SERVICE_ID, createNotification(playStatusTitle))
    }

    private fun startLocationUpdate() {
        locationManager =
            PlaygroundLocationManager(this) { location ->
                currentLocation = location
                sendLocationToBroadcast()
            }
        locationManager.startLocationUpdate()
    }

    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(
                WALK_SERVICE_CHANNEL_ID,
                WALK_SERVICE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createNotification(playStatusTitle: String): Notification {
        val intent =
            MainActivity.getIntent(this).apply {
                putExtra(EXTRA_FRAGMENT, PlaygroundFragment.TAG)
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        return NotificationCompat.Builder(this, WALK_SERVICE_CHANNEL_ID)
            .setContentTitle(playStatusTitle)
            .setContentText(resources.getString(R.string.woof_location_tracking))
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setUsesChronometer(true).setWhen(startMillis)
            .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false).setDefaults(NotificationCompat.DEFAULT_ALL).build()
    }

    private fun sendLocationToBroadcast() {
        val intent =
            Intent(ACTION_LOCATION_UPDATE).apply {
                putExtra(PlaygroundLocationReceiver.EXTRA_LOCATION, currentLocation)
            }
        sendBroadcast(intent)
    }

    private fun convertPlayStatusToTitle(playStatus: PlayStatus): String {
        return when (playStatus) {
            PlayStatus.PLAYING -> getString(R.string.playground_pet_is_playing)
            else -> getString(R.string.playground_pet_is_away)
        }
    }

    companion object {
        private const val WALK_SERVICE_CHANNEL_ID = "walk_service_id"
        private const val WALK_SERVICE_CHANNEL_NAME = "Walk Service"
        private const val EXTRA_PLAY_STATUS = "playStatus"
        private const val REQUEST_CODE_ID = 0
        private const val SERVICE_ID = 1

        fun getIntent(
            context: Context,
            playStatus: PlayStatus,
        ): Intent {
            return Intent(context, PlaygroundLocationService::class.java)
                .apply {
                    putExtra(EXTRA_PLAY_STATUS, playStatus)
                }
        }
    }
}
