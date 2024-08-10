package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import com.happy.friendogly.domain.error.ApiException
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.MessageType
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.utils.addSourceList
import com.happy.friendogly.presentation.utils.getSerializable
import okhttp3.MultipartBody

class ProfileSettingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val postMemberUseCase: PostMemberUseCase,
    private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
) : BaseViewModel() {
    val accessToken = savedStateHandle.get<String>(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN)

    private val _uiState: MutableLiveData<ProfileSettingUiState> =
        MutableLiveData(ProfileSettingUiState())
    val uiState: LiveData<ProfileSettingUiState> get() = _uiState

    val nickname = MutableLiveData<String>("")

    val isButtonActive =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                nickname,
                uiState,
            ) {
                val state = uiState.value ?: return@addSourceList false
                val nickname = nickname.value ?: return@addSourceList false

                !((nickname == state.beforeName || nickname.isBlank()) && state.profileImageUrl == state.beforeProfileImageUrl)
            }
        }

    private val _navigateAction: MutableLiveData<Event<ProfileSettingNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<ProfileSettingNavigationAction>> get() = _navigateAction

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        val profile =
            savedStateHandle.getSerializable(
                ProfileSettingActivity.PUT_EXTRA_PROFILE,
                Profile.serializer(),
            )
        profile ?: return

        val state = uiState.value ?: return

        nickname.value = profile.name
        _uiState.value =
            state.copy(
                isFirstTimeSetup = false,
                beforeName = profile.name,
                profileImageUrl = profile.imageUrl,
                beforeProfileImageUrl = profile.imageUrl,
            )
    }

    fun selectProfileImage() {
        _navigateAction.emit(ProfileSettingNavigationAction.NavigateToSetProfileImage)
    }

    fun submitProfileSelection() {
        launch {
            val nickname = nickname.value ?: return@launch
            if (nickname.isBlank()) return@launch
            if (regex.matches(nickname)) return@launch
            val state = uiState.value ?: return@launch

            if (state.isFirstTimeSetup) {
                postMember(nickname, state.profilePath)
            } else {
                patchMember(nickname, state.profilePath)
            }
        }
    }

    private suspend fun postMember(
        nickname: String,
        profilePath: MultipartBody.Part?,
    ) {
        accessToken ?: return

        // TODO email 필드는 임시로 넣어두었습니다.
        postMemberUseCase(
            name = nickname,
            email = "test@banggapge.com",
            file = profilePath,
            accessToken = accessToken,
        ).onSuccess { register ->
            saveJwaToken(register.tokens)
        }.onFailure {
            // TODO 예외처리
        }
    }

    private suspend fun saveJwaToken(jwtToken: JwtToken) {
        saveJwtTokenUseCase(jwtToken = jwtToken).onSuccess {
            _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
        }.onFailure { e ->
            sendErrorMessage(throwable = e, type = MessageType.SNACKBAR)
        }
    }

    private suspend fun patchMember(
        nickname: String,
        profilePath: MultipartBody.Part?,
    ) {
        // TODO patch use case 호출 예정
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = bitmap, profileImageUrl = null)
    }

    fun resetProfileImage() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = null, profileImageUrl = null)
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
            return BaseViewModelFactory { creator ->
                ProfileSettingViewModel(
                    savedStateHandle = creator.createSavedStateHandle(),
                    postMemberUseCase = postMemberUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                )
            }
        }
    }
}
