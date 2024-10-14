package com.happy.friendogly.presentation.ui.message.action

interface MessageNavigateAction {
    data class FinishMessageActivity(val messageUpdated: Boolean) : MessageNavigateAction
}
