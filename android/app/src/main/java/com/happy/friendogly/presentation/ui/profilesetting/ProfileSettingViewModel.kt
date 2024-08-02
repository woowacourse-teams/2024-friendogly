package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.error.ApiException
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.MessageType
import com.happy.friendogly.presentation.base.emit
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
        launch {
            val nickname = nickname.value ?: return@launch
            if (nickname.isBlank()) return@launch
            if (regex.matches(nickname)) return@launch
            val profilePath = _uiState.value?.profilePath

            // TODO email 필드는 임시로 넣어두었습니다.
            postMemberUseCase(
                name = nickname,
                email = "test@banggapge.com",
                file = profilePath,
            ).onSuccess { member ->
                saveJwaToken(member.id)
            }.onFailure { e ->
                when (e) {
                    is ApiException.BadRequest -> handleBadRequest(e)
                    else -> throw e
                }
            }
        }
    }

    private suspend fun saveJwaToken(memberId: Long) {
        val jwtToken = JwtToken(accessToken = memberId.toString(), refreshToken = null)
        saveJwtTokenUseCase(jwtToken = jwtToken).onSuccess {
            _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
        }.onFailure { e ->
            sendErrorMessage(throwable = e, type = MessageType.SNACKBAR)
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

    private fun handleBadRequest(exception: ApiException.BadRequest?) {
        val error = exception?.error ?: return
        // TODO 서버에서 내려주는 error code에 따라서 예외 처리
        return when (error.data.errorCode) {
            else -> sendErrorMessage(throwable = exception, type = MessageType.SNACKBAR)
        }
    }

    companion object {
        private val regex = "^[ㄱ-ㅎㅏ-ㅣ]+$".toRegex()

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
