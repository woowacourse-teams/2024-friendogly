package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.data.repository.HackathonRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.remote.dto.request.RequestMemberPostDto
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

var memberId: Long = 1

class ProfileSettingViewModel : BaseViewModel() {
    private val _uiState: MutableLiveData<ProfileSettingUiState> =
        MutableLiveData(ProfileSettingUiState())
    val uiState: LiveData<ProfileSettingUiState> get() = _uiState

    val nickname = MutableLiveData<String>("")

    private val _navigateAction: MutableLiveData<Event<ProfileSettingNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<ProfileSettingNavigationAction>> get() = _navigateAction

    fun selectProfileImage() {
        _navigateAction.emit(ProfileSettingNavigationAction.NavigateToSetProfileImage)
    }

    fun submitProfileSelection() {
        viewModelScope.launch {
            val request =
                RequestMemberPostDto(
                    name = nickname.value.toString(),
                )
            HackathonRepository.postMember(request).onSuccess { id ->
                memberId = id.toLong()
                _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
                Log.d("ttt memberId", memberId.toString())
                Log.d("ttt id", id.toString())
            }.onFailure {
                Log.d("ttt", it.toString())
            }
        }
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = bitmap)
    }

    fun resetProfileImage() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = null)
    }

    fun updateProfileFile(file: MultipartBody.Part) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = file)
    }
}
