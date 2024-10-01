package com.happy.friendogly.presentation.ui.club.common

import com.happy.friendogly.R

sealed interface MessageHandler {
    data class SendToast(val messageId: Int) : MessageHandler
    data class SendSnackBar(val messageId: Int) : MessageHandler
}

fun ClubErrorEvent.handleError(
    sendMessage: (MessageHandler) -> Unit,
) {
    when (this) {
        ClubErrorEvent.FileSizeError -> sendMessage(MessageHandler.SendToast(R.string.file_size_exceed_message))
        ClubErrorEvent.ServerError -> sendMessage(MessageHandler.SendToast(R.string.server_error_message))
        ClubErrorEvent.UnKnownError -> sendMessage(MessageHandler.SendToast(R.string.default_error_message))
        ClubErrorEvent.InternetError -> sendMessage(MessageHandler.SendSnackBar(R.string.no_internet_message))
    }
}
