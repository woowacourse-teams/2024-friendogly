package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetChatMemberUseCase
import com.happy.friendogly.domain.usecase.GetChatRoomClubUseCase
import com.happy.friendogly.domain.usecase.SaveChatAlarmUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class ChatInfoViewModel @Inject constructor(
    private val getChatRoomClubUseCase: GetChatRoomClubUseCase,
    private val getChatMemberUseCase: GetChatMemberUseCase,
    private val getChatAlarmUseCase: GetChatAlarmUseCase,
    private val saveChatAlarmUseCase: SaveChatAlarmUseCase,
) : BaseViewModel() {
    private val _clubInfo: MutableLiveData<ChatInfoUiModel> = MutableLiveData()
    val clubInfo: LiveData<ChatInfoUiModel> get() = _clubInfo

    private val _joiningPeople: MutableLiveData<List<JoinPeople>> = MutableLiveData()
    val joiningPeople: LiveData<List<JoinPeople>> get() = _joiningPeople

    private val _alarmSetting: MutableLiveData<Boolean> = MutableLiveData()
    val alarmSetting: LiveData<Boolean>
        get() = _alarmSetting

    fun getClubInfo(chatRoomId: Long): Deferred<Long> =
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

    fun getChatMember(chatRoomId: Long) {
        launch {
            getChatMemberUseCase.invoke(chatRoomId).onSuccess { members ->
                _joiningPeople.value = members.map { it.toUiModel(getClubInfo(chatRoomId).await()) }
            }.onFailure {
                // TODO 에러 처리
            }
        }
    }

    fun getAlamSetting() {
        launch {
            getChatAlarmUseCase().onSuccess {
                _alarmSetting.value = it
            }.onFailure {
                // TODO 에러 처리
            }
        }
    }

    fun saveAlamSetting(isChecked: Boolean) {
        launch {
            saveChatAlarmUseCase(isChecked).onFailure {
                // TODO 에러 처리
            }
        }
    }

    companion object {
        fun factory(
            getChatRoomClubUseCase: GetChatRoomClubUseCase,
            getChatMemberUseCase: GetChatMemberUseCase,
            getChatAlarmUseCase: GetChatAlarmUseCase,
            saveChatAlarmUseCase: SaveChatAlarmUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ChatInfoViewModel(
                    getChatRoomClubUseCase,
                    getChatMemberUseCase,
                    getChatAlarmUseCase,
                    saveChatAlarmUseCase,
                )
            }
        }
    }
}
