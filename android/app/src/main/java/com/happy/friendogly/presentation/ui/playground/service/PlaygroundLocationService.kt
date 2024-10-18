package com.happy.friendogly.presentation.ui.playground.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.MainActivity.Companion.EXTRA_FRAGMENT
import com.happy.friendogly.presentation.ui.playground.PlaygroundFragment
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationReceiver.Companion.ACTION_LEAVE_PLAYGROUND
import com.happy.friendogly.presentation.ui.playground.service.PlaygroundLocationReceiver.Companion.ACTION_UPDATE_LOCATION

class PlaygroundLocationService : Service() {
    private val locationManager: PlaygroundLocationManager by lazy {
        PlaygroundLocationManager(this) { location ->
            currentLocation = location
            sendLocationUpdateBroadcast()
        }
    }
    private var currentLocation: Location? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent?.action) {
            ACTION_START -> {
                val playStatus = intent.getSerializableExtra(EXTRA_PLAY_STATUS) as? PlayStatus
                val playStatusTitle = convertPlayStatusToTitle(playStatus)
                startForegroundService(playStatusTitle)
            }

            ACTION_STOP -> {
                sendLeavePlaygroundBroadcast()
                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.stopLocationUpdate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService(playStatusTitle: String) {
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this,
                SERVICE_ID,
                createNotification(playStatusTitle),
                FOREGROUND_SERVICE_TYPE_LOCATION,
            )
        } else {
            startForeground(SERVICE_ID, createNotification(playStatusTitle))
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

        val stopIntent =
            PendingIntent.getService(
                this,
                REQUEST_CODE_ID,
                Intent(this, PlaygroundLocationService::class.java).apply { action = ACTION_STOP },
                PendingIntent.FLAG_IMMUTABLE,
            )

        val stopAction =
            NotificationCompat.Action(
                R.drawable.ic_stop,
                resources.getString(R.string.playground_leave),
                stopIntent,
            )

        return NotificationCompat.Builder(this, WALK_SERVICE_CHANNEL_ID)
            .setContentTitle(playStatusTitle)
            .setContentText(resources.getString(R.string.woof_location_tracking))
            .setSmallIcon(R.drawable.ic_footprint).setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH).addAction(stopAction)
            .setAutoCancel(false).setShowWhen(false).setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun sendLocationUpdateBroadcast() {
        val intent =
            Intent(ACTION_UPDATE_LOCATION).apply {
                putExtra(PlaygroundLocationReceiver.EXTRA_LOCATION, currentLocation)
            }
        sendBroadcast(intent)
    }

    private fun sendLeavePlaygroundBroadcast() {
        val intent = Intent(ACTION_LEAVE_PLAYGROUND)
        sendBroadcast(intent)
    }

    private fun convertPlayStatusToTitle(playStatus: PlayStatus?): String {
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

        const val ACTION_START = "PLAY"
        const val ACTION_STOP = "STOP"

        fun getIntent(
            context: Context,
            playStatus: PlayStatus,
        ): Intent {
            return Intent(context, PlaygroundLocationService::class.java).apply {
                putExtra(EXTRA_PLAY_STATUS, playStatus)
            }
        }
    }
}
