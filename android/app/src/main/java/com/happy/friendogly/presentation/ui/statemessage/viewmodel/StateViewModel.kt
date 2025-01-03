package com.happy.friendogly.presentation.ui.statemessage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.PatchPlaygroundMessageUseCase
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.statemessage.action.StateMessageActionHandler
import com.happy.friendogly.presentation.ui.statemessage.action.StateMessageAlertAction
import com.happy.friendogly.presentation.ui.statemessage.action.StateMessageNavigateAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateViewModel
    @Inject
    constructor(
        private val patchPlaygroundMessageUseCase: PatchPlaygroundMessageUseCase,
    ) : ViewModel(), StateMessageActionHandler {
        private val _message: MutableLiveData<String> = MutableLiveData()
        val message: LiveData<String> get() = _message

        private val _alertAction: MutableLiveData<Event<StateMessageAlertAction>> = MutableLiveData()
        val alertAction: LiveData<Event<StateMessageAlertAction>> get() = _alertAction

        private val _navigateAction: MutableLiveData<Event<StateMessageNavigateAction>> =
            MutableLiveData()
        val navigateAction: LiveData<Event<StateMessageNavigateAction>> get() = _navigateAction

        override fun clickCancelBtn() {
            _navigateAction.emit(StateMessageNavigateAction.FinishStateMessageActivity(messageUpdated = false))
        }

        override fun clickConfirmBtn() {
            viewModelScope.launch {
                val newMessage = message.value ?: return@launch
                patchPlaygroundMessageUseCase(newMessage).fold(
                    onSuccess = {
                        _navigateAction.emit(
                            StateMessageNavigateAction.FinishStateMessageActivity(
                                messageUpdated = true,
                            ),
                        )
                    },
                    onError = {
                        _alertAction.emit(StateMessageAlertAction.AlertFailToPatchPlaygroundStateMessage)
                    },
                )
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
