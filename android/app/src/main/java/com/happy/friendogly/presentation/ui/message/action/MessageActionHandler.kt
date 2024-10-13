package com.happy.friendogly.presentation.ui.message.action

interface MessageActionHandler {
    fun clickCancelBtn()

    fun clickConfirmBtn(message: String)

    fun clearMessageBtn()
}
