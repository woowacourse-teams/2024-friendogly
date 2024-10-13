package com.happy.friendogly.presentation.ui.message.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.message.action.MessageActionHandler
import com.happy.friendogly.presentation.ui.message.action.MessageNavigateAction

class MessageViewModel : ViewModel(), MessageActionHandler {
    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> get() = _message

    private val _navigateAction: MutableLiveData<Event<MessageNavigateAction>> = MutableLiveData()
    val navigateAction: LiveData<Event<MessageNavigateAction>> get() = _navigateAction

    override fun clickCancelBtn() {
        _navigateAction.emit(MessageNavigateAction.FinishMessageActivity)
    }

    override fun clickConfirmBtn(message: String) {
    }

    override fun clearMessageBtn() {
        _message.value = DEFAULT_MESSAGE
    }

    fun updateMessage(message: String) {
        _message.value = message
    }

    companion object {
        private const val DEFAULT_MESSAGE = ""
    }
}
