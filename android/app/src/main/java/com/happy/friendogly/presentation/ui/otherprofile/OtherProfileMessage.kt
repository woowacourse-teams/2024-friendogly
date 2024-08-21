package com.happy.friendogly.presentation.ui.otherprofile

sealed interface OtherProfileMessage {
    data object DefaultErrorMessage : OtherProfileMessage

    data object ServerErrorMessage : OtherProfileMessage

    data object NoInternetMessage : OtherProfileMessage
}
