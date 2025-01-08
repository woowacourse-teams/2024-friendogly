package com.happy.friendogly.presentation.ui.setting

data class SettingUiState(
    val totalAlarmPushPermitted: Boolean = true,
    val chattingAlarmPushPermitted: Boolean = true,
    val clubAlarmPushPermitted: Boolean = true,
    val playgroundAlarmPushPermitted: Boolean = true,
)
