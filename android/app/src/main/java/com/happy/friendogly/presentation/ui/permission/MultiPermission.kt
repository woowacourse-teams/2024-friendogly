package com.happy.friendogly.presentation.ui.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

class MultiPermission private constructor(
    private val lifecycleOwnerRef: WeakReference<LifecycleOwner>,
    private val permissionActions: Map<PermissionType, (Boolean) -> Unit> = mapOf(),
    private var request: ActivityResultLauncher<Array<String>>? = null
) {

    fun addAlarmPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        return if (AlarmPermission.isValidPermissionSDK()) {
            MultiPermission(
                lifecycleOwnerRef,
                permissionActions.plus(Pair(PermissionType.Alarm, isPermitted)),
            )
        } else {
            this
        }
    }

    fun addLocationPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        return MultiPermission(
            lifecycleOwnerRef,
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

    fun createRequest(): MultiPermission {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")
        request = if (lifecycleOwner is AppCompatActivity) {
            lifecycleOwner.createRequest()
        } else {
            (lifecycleOwner as Fragment).createRequest()
        }
        return this
    }

    fun showDialog(): MultiPermission {

        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")

        val permissions =
            permissionActions.map {
                when (it.key) {
                    PermissionType.Alarm -> AlarmPermission.from(lifecycleOwner)
                    PermissionType.Location -> LocationPermission.from(lifecycleOwner)
                }
            }
        val showDialogPermissions =
            permissions.filter { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }
        val requestPermissions =
            permissions.filterNot { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }

        showDialogPermissions.forEach {
            it.createAlarmDialog(
                permissionActions[it.permissionType]
                    ?: error("유효하지 않은 값이 들어왔습니다"),
            ).show(getFragmentManager(), "TAG")
        }
        return MultiPermission(lifecycleOwnerRef, permissionActions.filterKeys {
            requestPermissions.map { it.permissionType }.contains(it)
        }, request)
    }

    private fun getFragmentManager(): FragmentManager {
        val lifecycleOwner = lifecycleOwnerRef.get() ?: error("$lifecycleOwnerRef is null")
        return if (lifecycleOwner is AppCompatActivity) {
            lifecycleOwner.supportFragmentManager
        } else {
            (lifecycleOwner as Fragment).parentFragmentManager
        }
    }

    fun launch() {
        request?.launch(permissionActions.keys.flatMap { it.permissions }.toTypedArray())
    }

    companion object {
        fun from(lifecycleOwner: LifecycleOwner): MultiPermission =
            MultiPermission(WeakReference(lifecycleOwner))
    }
}
