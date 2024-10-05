package com.happy.friendogly.presentation.ui.woof.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build

class WoofWalkReceiver(val updateFootprintRecentWalkStatus: (Location) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val location =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_LOCATION, Location::class.java) ?: return
            } else {
                intent.getParcelableExtra(EXTRA_LOCATION) ?: return
            }
        updateFootprintRecentWalkStatus(location)
    }

    companion object {
        const val EXTRA_LOCATION = "location"
        const val ACTION_LOCATION_UPDATE = "LOCATION_UPDATE"
    }
}
