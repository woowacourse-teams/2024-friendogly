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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.happy.friendogly.R
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultBlueAlertDialog
import java.lang.ref.WeakReference

class AlarmPermission private constructor(private val lifecycleOwnerRef: WeakReference<LifecycleOwner>) :
    Permission(PermissionType.Alarm) {
    private lateinit var request: ActivityResultLauncher<String>
    private lateinit var isPermitted: (Boolean) -> Unit
    fun createRequest(isPermitted: (Boolean) -> Unit = {}): AlarmPermission {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")
        request = if (lifecycleOwner is AppCompatActivity) {
            lifecycleOwner.createRequest(isPermitted)
        } else {
            (lifecycleOwner as Fragment).createRequest(isPermitted)
        }
        this.isPermitted = isPermitted
        return this
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun launch() {
        if (!hasPermissions() && shouldShowRequestPermissionRationale()) return
        request.launch(Manifest.permission.POST_NOTIFICATIONS)

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showDialog() {
        if (!hasPermissions() && shouldShowRequestPermissionRationale()) {
            val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")

            if (lifecycleOwner is AppCompatActivity) {
                createAlarmDialog(isPermitted).show(
                    lifecycleOwner.supportFragmentManager,
                    ALARM_DIALOG_TAG
                )
            } else {
                createAlarmDialog(isPermitted).show(
                    (lifecycleOwner as Fragment).parentFragmentManager,
                    ALARM_DIALOG_TAG
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun shouldShowRequestPermissionRationale(): Boolean {
        return getActivity()?.shouldShowRequestPermissionRationale(
            Manifest.permission.POST_NOTIFICATIONS,
        ) ?: error("${getActivity()} is null")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun hasPermissions(): Boolean {
        return getActivity()?.checkSelfPermission(
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun AppCompatActivity.createRequest(
        isGranted: (Boolean) -> Unit,
    ) = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        callback = isGranted,
    )

    private fun Fragment.createRequest(
        isGranted: (Boolean) -> Unit,
    ) = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        callback = isGranted,
    )

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
    }

    companion object {
        private const val ALARM_DIALOG_TAG = "alarmDialog"

        fun from(lifecycleOwner: LifecycleOwner): AlarmPermission =
            AlarmPermission(WeakReference(lifecycleOwner))

        fun isValidPermissionSDK(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}
