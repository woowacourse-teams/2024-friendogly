package com.happy.friendogly.presentation.ui.permission

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

sealed class Permission(val permissionType: PermissionType) {
    abstract fun shouldShowRequestPermissionRationale(): Boolean

    abstract fun hasPermissions(): Boolean

    abstract fun createAlarmDialog(): DialogFragment
}
