package com.happy.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.domain.usecase.ConnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.DisconnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.GetChatMessagesUseCase
import com.happy.friendogly.domain.usecase.GetChatRoomClubUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val getChatRoomClubUseCase: GetChatRoomClubUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
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

    fun subscribeMessage(chatRoomId: Long) {
        viewModelScope.launch {
            val myMemberId = myMemberId(chatRoomId).await()
            val chats = initChats(chatRoomId, myMemberId)

            connect().await()
            _chats.value = chats.await()
            subScribeMessageUseCase(chatRoomId, myMemberId).distinctUntilChanged().map {
                when (it) {
                    is ChatComponent.Date -> it.toUiModel()
                    is ChatComponent.Enter -> it.toUiModel()
                    is ChatComponent.Leave -> it.toUiModel()
                    is Message.Mine -> it.toUiModel()
                    is Message.Other -> it.toUiModel()
                }
            }.collect { newChat ->
                _chats.update { it.plus(newChat) }
            }
        }
    }

    private fun myMemberId(chatRoomId: Long): Deferred<Long> =
        viewModelScope.async {
            getChatRoomClubUseCase(chatRoomId).getOrThrow().myMemberId
        }

    private fun connect(): Deferred<Unit> =
        viewModelScope.async {
            connectWebsocketUseCase()
        }

    private fun initChats(
        chatRoomId: Long,
        myMemberId: Long,
    ): Deferred<List<ChatUiModel>> =
        viewModelScope.async {
            getChatMessagesUseCase(chatRoomId, myMemberId).getOrDefault(emptyList()).map {
                when (it) {
                    is ChatComponent.Date -> it.toUiModel()
                    is ChatComponent.Enter -> it.toUiModel()
                    is ChatComponent.Leave -> it.toUiModel()
                    is Message.Mine -> it.toUiModel()
                    is Message.Other -> it.toUiModel()
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
            getChatRoomClubUseCase: GetChatRoomClubUseCase,
            getChatMessagesUseCase: GetChatMessagesUseCase,
            connectWebsocketUseCase: ConnectWebsocketUseCase,
            disconnectWebsocketUseCase: DisconnectWebsocketUseCase,
            subScribeMessageUseCase: SubScribeMessageUseCase,
            publishSendMessageUseCase: PublishSendMessageUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatViewModel(
                    getChatRoomClubUseCase,
                    getChatMessagesUseCase,
                    connectWebsocketUseCase,
                    disconnectWebsocketUseCase,
                    subScribeMessageUseCase,
                    publishSendMessageUseCase,
                )
            }
        }
    }
}
