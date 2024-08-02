package com.happy.friendogly.presentation.base

sealed interface BaseMessage {
    data class Toast(val message: String) : BaseMessage

    data class Snackbar(val message: String) : BaseMessage
}

enum class MessageType {
    TOAST,
    SNACKBAR,
}
