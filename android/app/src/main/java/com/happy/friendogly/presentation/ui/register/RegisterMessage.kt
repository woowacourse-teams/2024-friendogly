package com.happy.friendogly.presentation.ui.register

sealed interface RegisterMessage {
    data object DefaultErrorMessage : RegisterMessage

    data object ServerErrorMessage : RegisterMessage

    data object TokenNotStoredErrorMessage : RegisterMessage

    data object NoInternetMessage : RegisterMessage
}
