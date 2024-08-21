package com.happy.friendogly.presentation.ui.profilesetting

sealed interface ProfileSettingMessage {
    data object DefaultErrorMessage : ProfileSettingMessage

    data object FileSizeExceedMessage : ProfileSettingMessage

    data object ServerErrorMessage : ProfileSettingMessage

    data object TokenNotStoredErrorMessage : ProfileSettingMessage

    data object NoInternetMessage : ProfileSettingMessage
}
