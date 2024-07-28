package com.happy.friendogly.presentation.ui.permission

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

class MultiPermission private constructor(
    activity: FragmentActivity,
    private val permissionActions: Map<PermissionType, (Boolean) -> Unit> = mapOf(),
) {
    private val activityRef = WeakReference(activity)

    fun addAlarmPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        return if (AlarmPermission.isValidPermissionSDK()) {
            MultiPermission(
                activity,
                permissionActions.plus(Pair(PermissionType.Alarm, isPermitted)),
            )
        } else {
            this
        }
    }

    fun addLocationPermission(isPermitted: (Boolean) -> Unit = {}): MultiPermission {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        return MultiPermission(
            activity,
            permissionActions.plus(Pair(PermissionType.Location, isPermitted)),
        )
    }

    fun request() {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        activity.registerForActivityResult(
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
        ).launch(permissionActions.keys.flatMap { it.permissions }.toTypedArray())
    }

    fun request(permissions: List<PermissionType>) {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        activity.registerForActivityResult(
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
        ).launch(permissions.flatMap { it.permissions }.toTypedArray())
    }

    fun requestAndShowDialog() {
        val activity = activityRef.get() ?: error("${activityRef.javaClass.simpleName} is null")
        val permissions =
            permissionActions.map {
                when (it.key) {
                    PermissionType.Alarm -> AlarmPermission(activity)
                    PermissionType.Location -> LocationPermission(activity)
                }
            }
        val showDialogPermissions =
            permissions.filter { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }
        val requestPermissions =
            permissions.filterNot { it.shouldShowRequestPermissionRationale() && !it.hasPermissions() }

        showDialogPermissions.forEach {
            it.createAlarmDialog(
                activity,
                permissionActions[it.permissionType]
                    ?: error("유효하지 않은 값이 들어왔습니다"),
            ).show(activity.supportFragmentManager, "TAG")
        }
        request(requestPermissions.map { it.permissionType })
    }

    companion object {
        fun from(activity: FragmentActivity): MultiPermission = MultiPermission(activity)
    }
}
