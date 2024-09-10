package com.happy.friendogly.presentation.ui.woof.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.MainActivity.Companion.EXTRA_FRAGMENT
import com.happy.friendogly.presentation.ui.woof.WoofFragment
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.service.WoofWalkReceiver.Companion.ACTION_LOCATION_UPDATED
import com.naver.maps.geometry.LatLng

class WoofWalkService : Service() {
    private lateinit var locationManager: WoofWalkLocationManager
    private lateinit var myFootprintMarkerPosition: LatLng
    private var currentLocation: Location? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        myFootprintMarkerPosition =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(EXTRA_MY_FOOTPRINT_MARKER_POSITION, LatLng::class.java)
            } else {
                intent?.getParcelableExtra(EXTRA_MY_FOOTPRINT_MARKER_POSITION)
            } ?: return super.onStartCommand(intent, flags, startId)
        val walkStatus = intent?.getSerializableExtra(EXTRA_WALK_STATUS) as WalkStatus
        val walkStatusTitle = convertWalkStatusToTitle(walkStatus)

        val startTimeMillis = intent.getLongExtra(EXTRA_START_MILLIS, 0)
        startForegroundService(walkStatusTitle, startTimeMillis)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.stopLocationUpdate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService(
        walkStatusTitle: String,
        startMillis: Long,
    ) {
        startLocationUpdate()
        createNotificationChannel()
        startForeground(SERVICE_ID, createNotification(walkStatusTitle, startMillis))
    }

    private fun startLocationUpdate() {
        locationManager =
            WoofWalkLocationManager(this) { location ->
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

    private fun createNotification(
        walkStatusTitle: String,
        startMillis: Long,
    ): Notification {
        val intent =
            MainActivity.getIntent(this).apply {
                putExtra(EXTRA_FRAGMENT, WoofFragment.TAG)
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT,
            )

        return NotificationCompat.Builder(this, WALK_SERVICE_CHANNEL_ID)
            .setContentTitle(walkStatusTitle)
            .setContentText(resources.getString(R.string.woof_location_tracking))
            .setSmallIcon(R.mipmap.ic_launcher).setUsesChronometer(true).setWhen(startMillis)
            .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false).setDefaults(NotificationCompat.DEFAULT_ALL).build()
    }

    private fun sendLocationToBroadcast() {
        val intent =
            Intent(ACTION_LOCATION_UPDATED).apply {
                putExtra(WoofWalkReceiver.EXTRA_LOCATION, currentLocation)
            }
        sendBroadcast(intent)
    }

    private fun convertWalkStatusToTitle(walkStatus: WalkStatus): String {
        return when (walkStatus) {
            WalkStatus.BEFORE -> return resources.getString(R.string.woof_status_before)
            WalkStatus.ONGOING -> return resources.getString(R.string.woof_status_ongoing)
            WalkStatus.AFTER -> return resources.getString(R.string.woof_status_after)
        }
    }

    companion object {
        private const val WALK_SERVICE_CHANNEL_ID = "walk_service_id"
        private const val WALK_SERVICE_CHANNEL_NAME = "Walk Service"
        private const val EXTRA_WALK_STATUS = "walkStatus"
        private const val EXTRA_START_MILLIS = "startMillis"
        private const val EXTRA_MY_FOOTPRINT_MARKER_POSITION = "myFootprintMarkerPosition"
        private const val REQUEST_CODE_ID = 0
        private const val SERVICE_ID = 1

        fun getIntent(
            context: Context,
            walkStatus: WalkStatus,
            startMillis: Long,
            myFootprintMarkerPosition: LatLng,
        ): Intent {
            return Intent(context, WoofWalkService::class.java).apply {
                putExtra(EXTRA_WALK_STATUS, walkStatus)
                putExtra(EXTRA_START_MILLIS, startMillis)
                putExtra(EXTRA_MY_FOOTPRINT_MARKER_POSITION, myFootprintMarkerPosition)
            }
        }
    }
}
