package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.JwtToken
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
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class ProfileSettingViewModel(
    private val saveAlarmTokenUseCase: SaveAlamTokenUseCase,
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
            _loading.emit(true)
            val nickname = nickname.value ?: return@launch
            if (nickname.isBlank()) return@launch
            if (regex.matches(nickname)) return@launch
            val state = uiState.value ?: return@launch

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
                    else -> _message.emit(ProfileSettingMessage.ServerErrorMessage)
                }
            },
        )
    }

    private suspend fun saveJwaToken(jwtToken: JwtToken) {
        saveJwtTokenUseCase(jwtToken = jwtToken).fold(
            onSuccess = {
                _navigateAction.emit(ProfileSettingNavigationAction.NavigateToHome)
            },
            onError = { error ->
                when (error) {
                    DataError.Local.TOKEN_NOT_STORED -> _message.emit(ProfileSettingMessage.TokenNotStoredErrorMessage)
                    else -> _message.emit(ProfileSettingMessage.DefaultErrorMessage)
                }
            },
        )
    }

    private fun saveAlarmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                launch { // TODO 에러 핸들링
                    saveAlarmTokenUseCase(token)
                }
            }
    }


    private suspend fun patchMember(
        nickname: String,
        profilePath: MultipartBody.Part?,
    ) {
        // TODO patch use case 호출 예정
        delay(3000)
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

    companion object {
        private val regex = "^[ㄱ-ㅎㅏ-ㅣ]+$".toRegex()

        fun factory(
            saveAlarmTokenUseCase: SaveAlamTokenUseCase,
            postMemberUseCase: PostMemberUseCase,
            saveJwtTokenUseCase: SaveJwtTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { creator ->
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    savedStateHandle = creator.createSavedStateHandle(),
                    postMemberUseCase = postMemberUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                )
            }
        }
    }
}
