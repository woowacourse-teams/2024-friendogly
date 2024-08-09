package com.happy.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.repository.ChatRepository
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatDateTime
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatListViewModel(
    private val chatRepository: ChatRepository,
) : BaseViewModel() {
    private val _chats: MutableLiveData<List<ChatListUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatListUiModel>> get() = _chats

    var memberId: Long = 0L

    fun getChats() {
        viewModelScope.launch {
            chatRepository.getChatList().onSuccess { room ->
                _chats.value = room.chatRooms.map { it.toUiModel() }
                memberId = room.myMemberId
            }
        }
    }

    companion object {
        fun factory(chatRepository: ChatRepository): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatListViewModel(
                    chatRepository,
                )
            }
        }
    }
}
