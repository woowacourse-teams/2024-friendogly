package com.woowacourse.friendogly.presentation.ui.woof

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.woowacourse.friendogly.presentation.ui.MainActivity

class WoofPermissionRequester(private val activity: Activity) {
    fun hasNotLocationPermissions(): Boolean {
        return !(
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun checkLocationPermissions(showSettingSnackbar: () -> Unit) {
        if (shouldNotShowRequestPermissionRationale()) {
            showSettingSnackbar()
        } else {
            requestLocationPermissions()
        }
    }

    private fun shouldNotShowRequestPermissionRationale(): Boolean {
        return !(
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
        )
    }

    private fun requestLocationPermissions() {
        val permissions =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )

        ActivityCompat.requestPermissions(
            activity,
            permissions,
            MainActivity.LOCATION_PERMISSION_REQUEST_CODE,
        )
    }
}
