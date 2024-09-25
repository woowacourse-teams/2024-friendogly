package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.usecase.GetFCMTokenUseCase
import com.happy.friendogly.domain.usecase.PatchMemberUseCase
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.utils.addSourceList
import com.happy.friendogly.presentation.utils.getSerializable
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val saveAlarmTokenUseCase: SaveAlamTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val postMemberUseCase: PostMemberUseCase,
    private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
    private val patchMemberUseCase: PatchMemberUseCase,
    private val getFCMTokenUseCase: GetFCMTokenUseCase,
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

                !(
                    (nickname == state.beforeName || nickname.isBlank()) &&
                        (state.profileImageUrl == state.beforeProfileImageUrl && state.profilePath == null)
                )
            }
        }

    private val _navigateAction: MutableLiveData<Event<ProfileSettingNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<ProfileSettingNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<ProfileSettingMessage>> = MutableLiveData(null)
    val message: LiveData<Event<ProfileSettingMessage>> get() = _message

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData(null)
    val loading: LiveData<Event<Boolean>> get() = _loading

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
        val imageUrl = if (profile.imageUrl.isNullOrBlank()) null else profile.imageUrl

        nickname.value = profile.name
        _uiState.value =
            state.copy(
                isFirstTimeSetup = false,
                beforeName = profile.name,
                profileImageUrl = imageUrl,
                beforeProfileImageUrl = imageUrl,
            )
    }

    fun selectProfileImage() {
        _navigateAction.emit(ProfileSettingNavigationAction.NavigateToSetProfileImage)
    }

    fun submitProfileSelection() {
        launch {
            if (isButtonActive.value == false) return@launch
            val nickname = nickname.value ?: return@launch
            if (nickname.isBlank()) return@launch
            if (regex.matches(nickname)) return@launch
            val state = uiState.value ?: return@launch

            _loading.emit(true)
            if (state.isFirstTimeSetup) {
                postMember(nickname, state.profilePath)
            } else {
                patchMember(nickname, state.profilePath)
            }
            _loading.emit(false)
        }
    }

    private suspend fun postMember(
        nickname: String,
        profilePath: MultipartBody.Part?,
    ) {
        accessToken ?: return

        postMemberUseCase(
            name = nickname,
            file = profilePath,
            accessToken = accessToken,
        ).fold(
            onSuccess = { register ->
                saveJwaToken(register.tokens)
                saveAlarmToken()
            },
            onError = { error ->
                when (error) {
                    DataError.Network.FILE_SIZE_EXCEED -> _message.emit(ProfileSettingMessage.FileSizeExceedMessage)
                    DataError.Network.NO_INTERNET -> _message.emit(ProfileSettingMessage.NoInternetMessage)
                    DataError.Network.SERVER_ERROR -> _message.emit(ProfileSettingMessage.ServerErrorMessage)
                    else -> _message.emit(ProfileSettingMessage.ServerErrorMessage)
                }
            },
        )
    }

    private suspend fun saveJwaToken(jwtToken: JwtToken) {
        saveJwtTokenUseCase(jwtToken = jwtToken).fold(
            onSuccess = {},
            onError = { error ->
                when (error) {
                    DataError.Local.TOKEN_NOT_STORED -> _message.emit(ProfileSettingMessage.TokenNotStoredErrorMessage)
                    else -> _message.emit(ProfileSettingMessage.DefaultErrorMessage)
                }
            },
        )
    }

    private suspend fun saveAlarmToken() {
        getFCMTokenUseCase().fold(
            onSuccess = { token ->
                saveAlarmTokenUseCase(token).getOrThrow()
                _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
            },
            onError = {
                _message.emit(ProfileSettingMessage.DefaultErrorMessage)
            },
        )
    }

    private suspend fun patchMember(
        nickname: String,
        profilePath: MultipartBody.Part?,
    ) {
        val state = uiState.value ?: return

        val imageUpdateType =
            when {
                state.profilePath != null -> ImageUpdateType.UPDATE
                state.profileImageUrl == state.beforeProfileImageUrl -> ImageUpdateType.NOT_UPDATE
                else -> ImageUpdateType.DELETE
            }

        patchMemberUseCase(
            name = nickname,
            imageUpdateType = imageUpdateType,
            file = profilePath,
        ).fold(
            onSuccess = {
                _navigateAction.emit(ProfileSettingNavigationAction.NavigateToMyPage)
            },
            onError = { error ->
                when (error) {
                    DataError.Network.FILE_SIZE_EXCEED -> _message.emit(ProfileSettingMessage.FileSizeExceedMessage)
                    DataError.Network.NO_INTERNET -> _message.emit(ProfileSettingMessage.NoInternetMessage)
                    DataError.Network.SERVER_ERROR -> _message.emit(ProfileSettingMessage.ServerErrorMessage)
                    else -> _message.emit(ProfileSettingMessage.ServerErrorMessage)
                }
            },
        )
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = bitmap, profileImageUrl = null)
    }

    fun resetProfileImage() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = null, profileImage = null, profileImageUrl = null)
    }

    fun updateProfileFile(file: MultipartBody.Part) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = file)
    }

    companion object {
        private val regex = "^[ㄱ-ㅎㅏ-ㅣ]+$".toRegex()

        fun factory(
            saveAlarmTokenUseCase: SaveAlamTokenUseCase,
            postMemberUseCase: PostMemberUseCase,
            saveJwtTokenUseCase: SaveJwtTokenUseCase,
            patchMemberUseCase: PatchMemberUseCase,
            getFCMTokenUseCase: GetFCMTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { creator ->
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    savedStateHandle = creator.createSavedStateHandle(),
                    postMemberUseCase = postMemberUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    patchMemberUseCase = patchMemberUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )
            }
        }
    }
}
