package com.happy.friendogly.presentation.ui.message.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.PathPlaygroundMessageUseCase
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.message.action.MessageActionHandler
import com.happy.friendogly.presentation.ui.message.action.MessageAlertAction
import com.happy.friendogly.presentation.ui.message.action.MessageNavigateAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
    @Inject
    constructor(
        private val patchPlaygroundMessageUseCase: PathPlaygroundMessageUseCase,
    ) : ViewModel(), MessageActionHandler {
        private val _message: MutableLiveData<String> = MutableLiveData()
        val message: LiveData<String> get() = _message

        private val _alertAction: MutableLiveData<Event<MessageAlertAction>> = MutableLiveData()
        val alertAction: LiveData<Event<MessageAlertAction>> get() = _alertAction

        private val _navigateAction: MutableLiveData<Event<MessageNavigateAction>> = MutableLiveData()
        val navigateAction: LiveData<Event<MessageNavigateAction>> get() = _navigateAction

        override fun clickCancelBtn() {
            _navigateAction.emit(MessageNavigateAction.FinishMessageActivity(messageUpdated = false))
        }

        override fun clickConfirmBtn() {
            viewModelScope.launch {
                val newMessage = message.value ?: return@launch
                patchPlaygroundMessageUseCase(newMessage).onSuccess {
                    _navigateAction.emit(MessageNavigateAction.FinishMessageActivity(messageUpdated = true))
                }.onFailure {
                    _alertAction.emit(MessageAlertAction.AlertFailToPatchPlaygroundMessage)
                }
            }
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
