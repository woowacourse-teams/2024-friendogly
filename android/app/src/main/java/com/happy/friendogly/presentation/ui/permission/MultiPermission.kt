package com.happy.friendogly.presentation.ui.permission

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

class MultiPermission private constructor(
    lifecycleOwner:LifecycleOwner,
    private val permissionActions: Map<PermissionType, (Boolean) -> Unit> = mapOf(),
) : DefaultLifecycleObserver {

    private val lifecycleOwnerRef = WeakReference(lifecycleOwner)

    private var request: ActivityResultLauncher<Array<String>> = if (lifecycleOwner is AppCompatActivity) {
        lifecycleOwner.createRequest()
    } else {
        (lifecycleOwner as Fragment).createRequest()
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

    fun requestAlarmPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        return if (AlarmPermission.isValidPermissionSDK()) {
            MultiPermission(
                requireNotNull(lifecycleOwnerRef.get()),
                permissionActions.plus(Pair(PermissionType.Alarm, isPermitted)),
            )
        } else {
            this
        }
    }

    fun requestLocationPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        return MultiPermission(
            requireNotNull(lifecycleOwnerRef.get()),
            permissionActions.plus(Pair(PermissionType.Location, isPermitted)),
        )
    }

    private fun AppCompatActivity.createRequest(): ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            callback = {
                val types =
                    it.mapKeys { map ->
                        PermissionType.findType(map.key)
                    }

                types.forEach { map ->
                    permissionActions[map.key]?.invoke(map.value)
                }
            },
        )

    private fun Fragment.createRequest(): ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            callback = {
                val types =
                    it.mapKeys { map ->
                        PermissionType.findType(map.key)
                    }

                types.forEach { map ->
                    permissionActions[map.key]?.invoke(map.value)
                }
            },
        )

    fun showDialog():MultiPermission {
        val permissions =
            permissionActions.map {
                when (it.key) {
                    PermissionType.Alarm -> AlarmPermission(requireNotNull(getActivity()))
                    PermissionType.Location -> LocationPermission(requireNotNull(getActivity()))
                }
            }
        val showDialogPermissions =
            permissions.filter { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }
        val requestPermissions =
            permissions.filterNot { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }

        showDialogPermissions.forEach {
            it.createAlarmDialog(
                requireNotNull(getActivity()),
                permissionActions[it.permissionType]
                    ?: error("유효하지 않은 값이 들어왔습니다"),
            ).show(requireNotNull(getActivity()).supportFragmentManager, "TAG")
        }
        return MultiPermission(requireNotNull(lifecycleOwnerRef.get()), permissionActions.filterKeys { requestPermissions.map { it.permissionType }.contains(it) })
    }

    private fun getActivity(): AppCompatActivity? =
        if (lifecycleOwnerRef.get() is Fragment) (lifecycleOwnerRef.get() as? Fragment)?.context?.scanForActivity() else lifecycleOwnerRef.get() as? AppCompatActivity



    fun launch() {
        request.launch(permissionActions.keys.flatMap { it.permissions }.toTypedArray())
    }

    companion object {
        fun from(lifecycleOwner: LifecycleOwner): MultiPermission =
            MultiPermission(lifecycleOwner)
    }
}
