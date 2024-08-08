package com.happy.friendogly.presentation.ui.chatlist.chat

import android.util.Log
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalTime

class ChatViewModel(
    private val webSocketRepository: WebSocketRepository,
) : BaseViewModel() {
    private val _chats: MutableLiveData<List<ChatUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatUiModel>> get() = _chats

    val sendMessage = MutableLiveData("")

    var memberId: Long = 0L

    val isCanSend =
        MediatorLiveData<Boolean>().apply {
            addSource(sendMessage) {
                value = it.isNotBlank()
            }
        }

    fun subscribeMessage(chatRoomId: Long) {
        viewModelScope.launch {
            val newChat = webSocketRepository.subscribeMessage(chatRoomId).map {
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

    fun inviteMember(chatRoomId: Long) {
        viewModelScope.launch {
            /*webSocketRepository.publishInvite(1)
            webSocketRepository.publishInvite(2)
            webSocketRepository.publishInvite(3)
            webSocketRepository.publishInvite(4)
            webSocketRepository.publishInvite(5)*/

        }
    }

    fun getChats(chatId: Long) {
        // _chats.value = dummyChats
    }

    fun sendMessage(chatRoomId: Long, content: String) {
        viewModelScope.launch {
            webSocketRepository.publishSend(chatRoomId, content)
        }
    }

    companion object {
        private val dummyChats =
            listOf(
                ChatUiModel.ComeOut("벼리", true),
                ChatUiModel.ComeOut("누누", true),
                ChatUiModel.Mine(
                    "이거 잘 작동되는거 맞냐", LocalTime.now(),
                ),
                ChatUiModel.Other(
                    "채드",
                    "https://m.segye.com/content/image/2" +
                            "021/07/29/20210729517145.jpg",
                    "화면 보이는거 보니깐 잘 작동하는듯 \n 근데 나 긴 텍스트 필요하니깐 아무말이나 쓸게 블라블랄",
                    LocalTime.now(),
                ),
            )

        fun factory(
            webSocketRepository: WebSocketRepository,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatViewModel(
                    webSocketRepository,
                )
            }
        }
    }
}
