package com.happy.friendogly.presentation.ui.permission

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.happy.friendogly.R
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultBlueAlertDialog
import java.lang.ref.WeakReference

class LocationPermission private constructor(private val lifecycleOwnerRef: WeakReference<LifecycleOwner>) :
    Permission(PermissionType.Location) {

    override fun hasPermissions(): Boolean {
        return getActivity()?.checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED &&
                getActivity()?.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getActivity(): AppCompatActivity? =
        if (lifecycleOwnerRef.get() is Fragment) (lifecycleOwnerRef.get() as? Fragment)?.context?.scanForActivity() else lifecycleOwnerRef.get() as? AppCompatActivity


    private fun Context.scanForActivity(): AppCompatActivity? {
        return when (this) {
            is AppCompatActivity -> this
            is ContextWrapper -> baseContext.scanForActivity()
            else -> {
                null
            }
        }
    }


    override fun createAlarmDialog(
        clickResult: (Boolean) -> Unit,
    ): DialogFragment {
        val activity = getActivity() ?: error("${getActivity()} is null")

        return DefaultBlueAlertDialog(
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
    }

    override fun shouldShowRequestPermissionRationale(): Boolean {
        val activity = getActivity() ?: error("${getActivity()} is null")

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
    }

    companion object {
        fun from(lifecycleOwner: LifecycleOwner): LocationPermission =
            LocationPermission(WeakReference(lifecycleOwner))
    }
}
