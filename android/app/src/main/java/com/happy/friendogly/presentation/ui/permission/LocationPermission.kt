package com.happy.friendogly.presentation.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.happy.friendogly.R
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultBlueAlertDialog
import com.happy.friendogly.presentation.ui.MainActivity
import java.lang.ref.WeakReference

class LocationPermission(activity: FragmentActivity) : Permission(PermissionType.Location) {
    private val activityRef = WeakReference(activity)

    override fun hasPermissions(): Boolean {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationPermissions(isPermitted: (Boolean) -> Unit) {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        if (!shouldShowRequestPermissionRationale()) {
            createAlarmDialog(activity, isPermitted).show(
                activity.supportFragmentManager,
                LOCATION_DIALOG_TAG,
            )
        } else {
            requestLocationPermissions()
        }
    }

    override fun createAlarmDialog(
        activity: FragmentActivity,
        clickResult: (Boolean) -> Unit,
    ): DialogFragment =
        DefaultBlueAlertDialog(
            alertDialogModel =
                AlertDialogModel(
                    activity.getString(R.string.location_dialog_title),
                    activity.getString(R.string.location_dialog_body),
                    activity.getString(R.string.permission_cancel),
                    activity.getString(R.string.permission_go_setting),
                ),
            clickToNegative = {
                clickResult(false)
            },
            clickToPositive = {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${activity.packageName}"))
                activity.startActivity(intent)
                clickResult(true)
            },
        )

    override fun shouldShowRequestPermissionRationale(): Boolean {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
    }

    private fun requestLocationPermissions() {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")

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

    companion object {
        private const val LOCATION_DIALOG_TAG = "locationDialog"
    }
}
