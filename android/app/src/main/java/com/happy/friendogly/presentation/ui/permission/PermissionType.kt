package com.happy.friendogly.presentation.ui.permission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS

sealed class PermissionType(val permissions: List<String>) {
    constructor(vararg permissions: String) : this(permissions.toList())

    data object Location : PermissionType(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    data object Alarm : PermissionType(getAlarmPermission())

    companion object {
        private val NONE = emptyList<String>()

        fun findType(permission: String): PermissionType =
            when (permission) {
                POST_NOTIFICATIONS -> Alarm
                ACCESS_COARSE_LOCATION -> Location
                ACCESS_FINE_LOCATION -> Location
                else -> error("$permission is invalid argument")
            }

        fun getAlarmPermission(): List<String> =
            if (AlarmPermission.isValidPermissionSDK()) {
                listOf(
                    POST_NOTIFICATIONS,
                )
            } else {
                NONE
            }
    }
}
