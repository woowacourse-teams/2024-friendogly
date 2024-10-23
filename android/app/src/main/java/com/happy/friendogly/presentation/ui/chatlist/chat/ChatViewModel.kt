package com.happy.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.FlowPreview
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

        private var newChatMessageCount: Int = 0

        private var myMemberId: Long = DEFAULT_MEMBER_ID

        val isCanSend =
            MediatorLiveData<Boolean>().apply {
                addSource(sendMessage) {
                    value = it.isNotBlank()
                }
            }

        fun subscribeMessage(chatRoomId: Long) {
            launch {
                connectWebsocketUseCase()
                myMemberId = getChatRoomClubUseCase(chatRoomId).getOrThrow().myMemberId
                getChatMessage(chatRoomId, INITIAL_POSITION)

                subScribeMessageUseCase(chatRoomId, myMemberId).collect { newMessage ->
                    _chats.value = listOf(newMessage.toUiModel()) + _chats.value
                    _newChatEvent.emit()
                    launch {
                        newChatMessageCount += 1
                        saveChatMessageUseCase(chatRoomId, newMessage)
                    }
                }
            }
        }

        @OptIn(FlowPreview::class)
        fun getChatMessage(
            chatRoomId: Long,
            currentPosition: Int,
        ) {
            launch {
                getChatMessagesUseCase(
                    myMemberId = myMemberId,
                    chatRoomId = chatRoomId,
                    offset = currentPosition,
                    limit = PAGING_MESSAGE_SIZE,
                ).debounce(DEBOUNCE_TIME_OUT).collect {
                    _chats.value = _chats.value.plus(it.map { it.toUiModel() })
                }
            }
        }

        fun sendMessage(
            chatRoomId: Long,
            content: String,
        ) {
            launch {
                publishSendMessageUseCase(chatRoomId, content)
            }
        }

        override fun onCleared() {
            launch {
                disconnectWebsocketUseCase()
            }
            super.onCleared()
        }

        companion object {
            private const val DEFAULT_MEMBER_ID = -1L
            private const val INITIAL_POSITION = 0
            private const val PAGING_MESSAGE_SIZE = 50
            private const val DEBOUNCE_TIME_OUT = 500L
        }
    }
