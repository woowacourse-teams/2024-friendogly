package com.happy.friendogly.presentation.ui.playground.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build

class PlaygroundLocationReceiver(
    val updateLocation: (Location) -> Unit,
    val leavePlayground: () -> Unit,
) :
    BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        when (intent.action) {
            ACTION_UPDATE_LOCATION -> {
                val location =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(EXTRA_LOCATION, Location::class.java) ?: return
                    } else {
                        intent.getParcelableExtra(EXTRA_LOCATION) ?: return
                    }
                updateLocation(location)
            }

            ACTION_LEAVE_PLAYGROUND -> leavePlayground()
        }
    }

    companion object {
        const val EXTRA_LOCATION = "location"
        const val ACTION_UPDATE_LOCATION = "UPDATE_LOCATION"
        const val ACTION_LEAVE_PLAYGROUND = "LEAVE_PLAYGROUND"
    }
}
