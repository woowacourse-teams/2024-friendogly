package com.happy.friendogly.presentation.ui.permission

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.happy.friendogly.R
import com.happy.friendogly.firebase.analytics.AnalyticsHelper

import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultCoralAlertDialog
import com.happy.friendogly.presentation.utils.logPermissionLocationDenied
import java.lang.ref.WeakReference

class LocationPermission private constructor(
    private val lifecycleOwnerRef: WeakReference<LifecycleOwner>,
    private val analyticsHelper: AnalyticsHelper,
    private val isPermitted: (Boolean) -> Unit,
) : Permission(PermissionType.Location) {
    private val settingStartActivity: ActivityResultLauncher<Intent>

    init {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")

        settingStartActivity =
            if (lifecycleOwner is AppCompatActivity) {
                lifecycleOwner.createStartActivity()
            } else {
                (lifecycleOwner as Fragment).createStartActivity()
            }
    }

    private fun AppCompatActivity.createStartActivity() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (hasPermissions()) {
                isPermitted(true)
            } else {
                isPermitted(false)
                analyticsHelper.logPermissionLocationDenied()
            }
        }

    private fun Fragment.createStartActivity() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (hasPermissions()) {
                isPermitted(true)
            } else {
                isPermitted(false)
                analyticsHelper.logPermissionLocationDenied()
            }
        }

    override fun hasPermissions(): Boolean {
        return getActivity()?.checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED &&
                getActivity()?.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getActivity(): AppCompatActivity? =
        if (lifecycleOwnerRef.get() is Fragment) {
            (lifecycleOwnerRef.get() as? Fragment)?.context?.scanForActivity()
        } else {
            lifecycleOwnerRef.get() as? AppCompatActivity
        }

    private fun Context.scanForActivity(): AppCompatActivity? {
        return when (this) {
            is AppCompatActivity -> this
            is ContextWrapper -> baseContext.scanForActivity()
            else -> {
                null
            }
        }
    }

    private fun AppCompatActivity.createDialog(): DialogFragment =
        DefaultCoralAlertDialog(
            alertDialogModel =
            AlertDialogModel(
                getString(R.string.location_dialog_title),
                getString(R.string.location_dialog_body),
                getString(R.string.permission_cancel),
                getString(R.string.permission_go_setting),
            ),
            clickToNegative = {
                isPermitted(false)
                analyticsHelper.logPermissionLocationDenied()
            },
            clickToPositive = {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:$packageName"))
                settingStartActivity.launch(intent)
            },
        )

    private fun Fragment.createDialog(): DialogFragment =
        DefaultCoralAlertDialog(
            alertDialogModel =
            AlertDialogModel(
                getString(R.string.location_dialog_title),
                getString(R.string.location_dialog_body),
                getString(R.string.permission_cancel),
                getString(R.string.permission_go_setting),
            ),
            clickToNegative = {
                isPermitted(false)
                analyticsHelper.logPermissionLocationDenied()
            },
            clickToPositive = {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${requireActivity().packageName}"))
                settingStartActivity.launch(intent)
            },
        )

    override fun createAlarmDialog(): DialogFragment {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")
        return if (lifecycleOwner is AppCompatActivity) {
            lifecycleOwner.createDialog()
        } else {
            (lifecycleOwner as Fragment).createDialog()
        }
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
        fun from(
            lifecycleOwner: LifecycleOwner,
            analyticsHelper: AnalyticsHelper,
            isPermitted: (Boolean) -> Unit,
        ): LocationPermission =
            LocationPermission(WeakReference(lifecycleOwner), analyticsHelper, isPermitted)
    }
}
