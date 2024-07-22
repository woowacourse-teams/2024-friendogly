package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.model.JwtToken
import com.woowacourse.friendogly.domain.usecase.PostMemberUseCase
import com.woowacourse.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileSettingViewModel(
    private val postMemberUseCase: PostMemberUseCase,
    private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
) : BaseViewModel() {
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
        val nickname = nickname.value ?: return
        if (nickname.isBlank()) return
        viewModelScope.launch {
            postMemberUseCase(name = nickname, email = "wnswkd486@gmail.com").onSuccess { member ->
                saveJwaToken(member.id)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    private suspend fun saveJwaToken(memberId: Long) {
        val jwtToken = JwtToken(accessToken = memberId.toString(), refreshToken = null)
        saveJwtTokenUseCase(jwtToken = jwtToken).onSuccess {
            _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
        }.onFailure {
            // TODO 예외처리
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

    companion object {
        fun factory(
            postMemberUseCase: PostMemberUseCase,
            saveJwtTokenUseCase: SaveJwtTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                ProfileSettingViewModel(
                    postMemberUseCase = postMemberUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                )
            }
        }
    }
}
