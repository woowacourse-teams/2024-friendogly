package com.happy.friendogly.presentation.ui.statemessage.action

interface StateMessageNavigateAction {
    data class FinishStateMessageActivity(val messageUpdated: Boolean) : StateMessageNavigateAction
}
