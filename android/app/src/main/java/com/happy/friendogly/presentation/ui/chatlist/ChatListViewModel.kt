package com.happy.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetChatListUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel
    @Inject
    constructor(
        private val getChatListUseCase: GetChatListUseCase,
    ) : BaseViewModel() {
        private val _chats: MutableLiveData<List<ChatListUiModel>> = MutableLiveData()
        val chats: LiveData<List<ChatListUiModel>> get() = _chats

        var memberId: Long = 0L
            private set

        private var pollJob: Job? = null

        val isChatEmpty =
            MediatorLiveData<Boolean>().apply {
                addSource(_chats) {
                    value = it.isEmpty()
                }
            }

        fun getPollingChats() {
            pollJob?.cancel()
            pollJob =
                viewModelScope.launch {
                    while (this.isActive) {
                        getChatListUseCase.invoke().onSuccess { room ->
                            _chats.value = room.chatRooms.map { it.toUiModel() }
                            memberId = room.myMemberId
                        }
                        delay(1000)
                    }
                }
        }

        fun cancelPolling() {
            pollJob?.cancel()
        }
    }
