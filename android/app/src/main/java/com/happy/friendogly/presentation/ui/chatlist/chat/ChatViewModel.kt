package com.happy.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.domain.repository.WebSocketRepository
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.ui.chatlist.uimodel.toUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalTime

class ChatViewModel(
    private val webSocketRepository: WebSocketRepository,
) : BaseViewModel() {
    private val _chats: MutableLiveData<List<ChatUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatUiModel>> get() = _chats

    val sendMessage = MutableLiveData("")

    val isCanSend =
        MediatorLiveData<Boolean>().apply {
            addSource(sendMessage) {
                value = it.isNotBlank()
            }
        }

    fun subscribeMessage(chatRoomId: Long) {
        viewModelScope.launch {
            val newChat =
                webSocketRepository.subscribeMessage(chatRoomId).map {
                    when (it) {
                        is ChatComponent.Date -> it.toUiModel()
                        is ChatComponent.Enter -> it.toUiModel()
                        is ChatComponent.Leave -> it.toUiModel()
                        is Message.Mine -> it.toUiModel()
                        is Message.Other -> it.toUiModel()
                    }
                }

            newChat.collect {
                _chats.value = (_chats.value ?: emptyList()) + listOf(it)
            }
        }
    }

    fun sendMessage(
        chatRoomId: Long,
        content: String,
    ) {
        viewModelScope.launch {
            webSocketRepository.publishSend(chatRoomId, content)
        }
    }

    companion object {
        fun factory(webSocketRepository: WebSocketRepository): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatViewModel(
                    webSocketRepository,
                )
            }
        }
    }
}
