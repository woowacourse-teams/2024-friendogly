package com.happy.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetChatListUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatListUseCase: GetChatListUseCase,
) : BaseViewModel() {
    private val _chats: MutableLiveData<List<ChatListUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatListUiModel>> get() = _chats

    var memberId: Long = 0L
        private set

    val isChatEmpty =
        MediatorLiveData<Boolean>().apply {
            addSource(_chats) {
                value = it.isEmpty()
            }
        }

    fun getChats() {
        viewModelScope.launch {
            getChatListUseCase.invoke().onSuccess { room ->
                _chats.value = room.chatRooms.map { it.toUiModel() }
                memberId = room.myMemberId
            }
        }
    }

    companion object {
        fun factory(getChatListUseCase: GetChatListUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatListViewModel(
                    getChatListUseCase,
                )
            }
        }
    }
}
