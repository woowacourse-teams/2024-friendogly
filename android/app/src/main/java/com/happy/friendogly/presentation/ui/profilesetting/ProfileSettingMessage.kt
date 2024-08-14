package com.happy.friendogly.presentation.ui.profilesetting

sealed interface ProfileSettingMessage {
    data object FileSizeExceedMessage : ProfileSettingMessage

    data object ServerErrorMessage : ProfileSettingMessage
}
