package com.happy.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.domain.usecase.ConnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.DisconnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.PublishSendMessageUseCase
import com.happy.friendogly.domain.usecase.SubScribeMessageUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.ui.chatlist.uimodel.toUiModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatViewModel(
    private val connectWebsocketUseCase: ConnectWebsocketUseCase,
    private val disconnectWebsocketUseCase: DisconnectWebsocketUseCase,
    private val subScribeMessageUseCase: SubScribeMessageUseCase,
    private val publishSendMessageUseCase: PublishSendMessageUseCase,
) : BaseViewModel() {
    private val _chats: MutableStateFlow<List<ChatUiModel>> = MutableStateFlow(emptyList())
    val chats: StateFlow<List<ChatUiModel>> get() = _chats.asStateFlow()

    val sendMessage = MutableLiveData("")

    val isCanSend =
        MediatorLiveData<Boolean>().apply {
            addSource(sendMessage) {
                value = it.isNotBlank()
            }
        }

    private val initJob: Deferred<Unit> =
        viewModelScope.async {
            connectWebsocketUseCase()
        }

    fun subscribeMessage(
        chatRoomId: Long,
        myMemberId: Long,
    ) {
        viewModelScope.launch {
            initJob.await()
            val newChat =
                subScribeMessageUseCase(chatRoomId, myMemberId).distinctUntilChanged().map {
                    when (it) {
                        is ChatComponent.Date -> it.toUiModel()
                        is ChatComponent.Enter -> it.toUiModel()
                        is ChatComponent.Leave -> it.toUiModel()
                        is Message.Mine -> it.toUiModel()
                        is Message.Other -> it.toUiModel()
                    }
                }

            newChat.collect {
                _chats.value += listOf(it)
            }
        }
    }

    fun sendMessage(
        chatRoomId: Long,
        content: String,
    ) {
        viewModelScope.launch {
            publishSendMessageUseCase(chatRoomId, content)
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            disconnectWebsocketUseCase()
        }
        super.onCleared()
    }

    companion object {
        fun factory(
            connectWebsocketUseCase: ConnectWebsocketUseCase,
            disconnectWebsocketUseCase: DisconnectWebsocketUseCase,
            subScribeMessageUseCase: SubScribeMessageUseCase,
            publishSendMessageUseCase: PublishSendMessageUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatViewModel(
                    connectWebsocketUseCase,
                    disconnectWebsocketUseCase,
                    subScribeMessageUseCase,
                    publishSendMessageUseCase,
                )
            }
        }
    }
}
