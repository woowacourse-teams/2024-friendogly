package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetChatMemberUseCase
import com.happy.friendogly.domain.usecase.GetChatRoomClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChatInfoViewModel(
    private val getChatRoomClubUseCase: GetChatRoomClubUseCase,
    private val getChatMemberUseCase: GetChatMemberUseCase,
) : BaseViewModel() {
    private val _clubInfo: MutableLiveData<ChatInfoUiModel> = MutableLiveData()
    val clubInfo: LiveData<ChatInfoUiModel> get() = _clubInfo

    private val _joiningPeople: MutableLiveData<List<JoinPeople>> = MutableLiveData()
    val joiningPeople: LiveData<List<JoinPeople>> get() = _joiningPeople

    fun getClubInfo(chatRoomId: Long):Deferred<Long> =
        viewModelScope.async {
            val clubInfo = getChatRoomClubUseCase(chatRoomId).getOrThrow()
            _clubInfo.value =
                ChatInfoUiModel(
                    clubId = clubInfo.clubId,
                    dogSize = clubInfo.allowedSize,
                    dogGender = clubInfo.allowedGender,
                )
            return@async clubInfo.clubId
        }


    fun getChatMember(
        chatRoomId: Long,
    ) {
        launch {
            getChatMemberUseCase.invoke(chatRoomId).onSuccess { members ->
                _joiningPeople.value = members.map { it.toUiModel(getClubInfo(chatRoomId).await()) }
            }.onFailure {
                // TODO 에러 처리
            }
        }
    }

    companion object {
        fun factory(
            getChatRoomClubUseCase: GetChatRoomClubUseCase,
            getChatMemberUseCase: GetChatMemberUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatInfoViewModel(
                    getChatRoomClubUseCase,
                    getChatMemberUseCase,
                )
            }
        }
    }
}
