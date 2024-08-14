package com.happy.friendogly.presentation.ui.register

sealed interface RegisterMessage {
    data object DefaultErrorMessage : RegisterMessage

    data object ServerErrorMessage : RegisterMessage
}
