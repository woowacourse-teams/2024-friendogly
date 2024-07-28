package com.happy.friendogly.presentation.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.happy.friendogly.R
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultBlueAlertDialog
import java.lang.ref.WeakReference

class AlarmPermission(activity: FragmentActivity) : Permission(PermissionType.Alarm) {
    private val activityRef = WeakReference(activity)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun askNotificationPermission(isPermitted: (Boolean) -> Unit = {}) {
        val activity = activityRef.get() ?: return
        if (!hasPermissions()) {
            if (shouldShowRequestPermissionRationale()
            ) {
                createAlarmDialog(activity, isPermitted).show(
                    activity.supportFragmentManager,
                    ALARM_DIALOG_TAG,
                )
            } else {
                requestPermissionLauncher(
                    activity,
                    isPermitted,
                ).launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun shouldShowRequestPermissionRationale(): Boolean {
        val activity = activityRef.get() ?: return false
        return activity.shouldShowRequestPermissionRationale(
            Manifest.permission.POST_NOTIFICATIONS,
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun hasPermissions(): Boolean {
        val activity = activityRef.get() ?: return false
        return activity.checkSelfPermission(
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionLauncher(
        activity: FragmentActivity,
        isGranted: (Boolean) -> Unit,
    ) = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        callback = isGranted,
    )

    override fun createAlarmDialog(
        activity: FragmentActivity,
        clickResult: (Boolean) -> Unit,
    ): DialogFragment =
        DefaultBlueAlertDialog(
            alertDialogModel =
                AlertDialogModel(
                    activity.getString(R.string.alarm_dialog_title),
                    activity.getString(R.string.alarm_dialog_body),
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

    companion object {
        private const val ALARM_DIALOG_TAG = "alarmDialog"

        fun isValidPermissionSDK(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}
