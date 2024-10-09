package com.happy.friendogly.presentation.ui.chatlist.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.ConnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.DisconnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.GetChatMessagesUseCase
import com.happy.friendogly.domain.usecase.GetChatRoomClubUseCase
import com.happy.friendogly.domain.usecase.PublishSendMessageUseCase
import com.happy.friendogly.domain.usecase.SaveChatMessageUseCase
import com.happy.friendogly.domain.usecase.SubScribeMessageUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.chatlist.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val getChatRoomClubUseCase: GetChatRoomClubUseCase,
        private val getChatMessagesUseCase: GetChatMessagesUseCase,
        private val connectWebsocketUseCase: ConnectWebsocketUseCase,
        private val disconnectWebsocketUseCase: DisconnectWebsocketUseCase,
        private val subScribeMessageUseCase: SubScribeMessageUseCase,
        private val publishSendMessageUseCase: PublishSendMessageUseCase,
        private val saveChatMessageUseCase: SaveChatMessageUseCase,
    ) : BaseViewModel() {
        private val _chats: MutableStateFlow<List<ChatUiModel>> =
            MutableStateFlow(emptyList())
        val chats: StateFlow<List<ChatUiModel>> get() = _chats.asStateFlow()

        val sendMessage = MutableLiveData("")

        private val _newChatEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
        val newChatEvent: LiveData<Event<Unit>> get() = _newChatEvent

        private var myMemberId: Long = 0L

        val isCanSend =
            MediatorLiveData<Boolean>().apply {
                addSource(sendMessage) {
                    value = it.isNotBlank()
                }
            }

        fun subscribeMessage(chatRoomId: Long) {
            viewModelScope.launch {
                connect().await()
                myMemberId = myMemberId(chatRoomId).await()
                async {
                    getChatMessagesUseCase(
                        myMemberId = myMemberId,
                        chatRoomId = chatRoomId,
                        offset = 0,
                        limit = 15,
                    ).collect {
                        _chats.value = it.map { it.toUiModel() }
                    }
                }.await()

                launch {
                    subScribeMessageUseCase(
                        chatRoomId = chatRoomId,
                        myMemberId = myMemberId,
                    ).collect {
                        _chats.value = listOf(it.toUiModel()) + chats.value
                        _newChatEvent.emit()
                        launch {
                            saveChatMessageUseCase(chatRoomId, it)
                        }
                    }
                }
            }
        }

        fun getChatMessage(
            chatRoomId: Long,
            currentPosition: Int,
        ) {
            launch {
                getChatMessagesUseCase(
                    myMemberId = myMemberId,
                    chatRoomId = chatRoomId,
                    offset = currentPosition,
                    limit = 15,
                ).debounce(500).collect {
                    _chats.value = _chats.value.plus(it.map { it.toUiModel() })
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
    }
