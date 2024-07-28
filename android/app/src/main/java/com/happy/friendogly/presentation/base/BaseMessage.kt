package com.happy.friendogly.presentation.base

sealed class BaseMessage(message: String) {
    data class Toast(val message: String) : BaseMessage(message)

    data class Snackbar(val message: String) : BaseMessage(message)
}

enum class MessageType {
    TOAST,
    SNACKBAR,
}
