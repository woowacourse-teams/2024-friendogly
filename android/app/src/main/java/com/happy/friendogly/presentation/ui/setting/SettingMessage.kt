package com.happy.friendogly.presentation.ui.setting

sealed interface SettingMessage {
    data object DefaultErrorMessage : SettingMessage

    data object ServerErrorMessage : SettingMessage

    data object TokenNotStoredErrorMessage : SettingMessage
}
