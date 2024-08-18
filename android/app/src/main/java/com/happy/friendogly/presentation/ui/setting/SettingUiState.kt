package com.happy.friendogly.presentation.ui.setting

data class SettingUiState(
    val totalAlarmPushPermitted: Boolean = false,
    val chattingAlarmPushPermitted: Boolean = false,
    val clubAlarmPushPermitted: Boolean = false,
    val woofAlarmPushPermitted:Boolean = false,
)
