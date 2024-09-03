package com.happy.friendogly.presentation.ui.permission

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultCoralAlertDialog
import com.happy.friendogly.presentation.utils.logPermissionAlarmDenied
import java.lang.ref.WeakReference

class AlarmPermission private constructor(
    private val lifecycleOwnerRef: WeakReference<LifecycleOwner>,
    private val isPermitted: (Boolean) -> Unit,
) : Permission(PermissionType.Alarm) {
    private lateinit var request: ActivityResultLauncher<String>
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
                AppModule.getInstance().analyticsHelper.logPermissionAlarmDenied()
            } else {
                isPermitted(false)
            }
        }

    private fun Fragment.createStartActivity() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (hasPermissions()) {
                isPermitted(true)
                AppModule.getInstance().analyticsHelper.logPermissionAlarmDenied()
            } else {
                isPermitted(false)
            }
        }

    fun createRequest(isPermitted: (Boolean) -> Unit = {}): AlarmPermission {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")
        request =
            if (lifecycleOwner is AppCompatActivity) {
                lifecycleOwner.createRequest(isPermitted)
            } else {
                (lifecycleOwner as Fragment).createRequest(isPermitted)
            }
        return this
    }

    fun launch() {
        if (!hasPermissions() && shouldShowRequestPermissionRationale()) return
        request.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun shouldShowRequestPermissionRationale(): Boolean {
        return getActivity()?.shouldShowRequestPermissionRationale(
            Manifest.permission.POST_NOTIFICATIONS,
        ) ?: error("${getActivity()} is null")
    }

    override fun hasPermissions(): Boolean {
        return getActivity()?.checkSelfPermission(
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun AppCompatActivity.createRequest(isGranted: (Boolean) -> Unit) =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            callback = isGranted,
        )

    private fun Fragment.createRequest(isGranted: (Boolean) -> Unit) =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            callback = isGranted,
        )

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
                    getString(R.string.alarm_dialog_title),
                    getString(R.string.alarm_dialog_body),
                    getString(R.string.permission_cancel),
                    getString(R.string.permission_go_setting),
                ),
            clickToNegative = {
                isPermitted(false)
                AppModule.getInstance().analyticsHelper.logPermissionAlarmDenied()
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
                    getString(R.string.alarm_dialog_title),
                    getString(R.string.alarm_dialog_body),
                    getString(R.string.permission_cancel),
                    getString(R.string.permission_go_setting),
                ),
            clickToNegative = {
                isPermitted(false)
                AppModule.getInstance().analyticsHelper.logPermissionAlarmDenied()
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

    companion object {
        fun from(
            lifecycleOwner: LifecycleOwner,
            isPermitted: (Boolean) -> Unit,
        ): AlarmPermission = AlarmPermission(WeakReference(lifecycleOwner), isPermitted)

        fun isValidPermissionSDK(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}
