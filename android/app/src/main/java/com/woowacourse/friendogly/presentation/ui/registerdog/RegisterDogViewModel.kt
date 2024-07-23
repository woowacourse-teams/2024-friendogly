package com.woowacourse.friendogly.presentation.ui.registerdog

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.usecase.PostPetUseCase
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.utils.addSourceList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class RegisterDogViewModel(
    private val postPetUseCase: PostPetUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<RegisterDogUiState> =
        MutableLiveData(RegisterDogUiState())
    val uiState: LiveData<RegisterDogUiState> get() = _uiState

    private val _profileImage: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val profileImage: LiveData<Bitmap?> get() = _profileImage

    val dogName = MutableLiveData<String>("")
    val dogDescription = MutableLiveData<String>("")
    private var dogSize: DogSize = DogSize.SMALL
    private var dogGender: DogGender = DogGender.MAIL

    val isProfileComplete =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                dogName,
                dogDescription,
                profileImage,
            ) {
                val name = dogName.value ?: return@addSourceList false
                val description = dogDescription.value ?: return@addSourceList false

                name.isNotBlank() && description.isNotBlank() && profileImage.value != null
            }
        }

    private val _navigateAction: MutableLiveData<Event<RegisterDogNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterDogNavigationAction>> get() = _navigateAction

    fun executeBackAction() {
        _navigateAction.emit(RegisterDogNavigationAction.NavigateToBack)
    }

    fun selectDogProfileImage() {
        _navigateAction.emit(RegisterDogNavigationAction.NavigateToSetProfileImage)
    }

    fun selectDogBirthday() {
        val state = _uiState.value ?: return

        _navigateAction.emit(
            RegisterDogNavigationAction.NavigateToSetBirthday(
                state.dogBirthdayYear,
                state.dogBirthdayMonth,
            ),
        )
    }

    fun updateDogProfileImage(bitmap: Bitmap) {
        _profileImage.value = bitmap
    }

    fun updateDogProfileFile(file: MultipartBody.Part) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = file)
    }

    fun updateDogSize(dogSize: DogSize) {
        this.dogSize = dogSize
    }

    fun updateDogGender(dogGender: DogGender) {
        this.dogGender = dogGender
    }

    fun updateNeutering() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(neutering = !state.neutering)
    }

    fun updateDogBirthday(
        year: Int,
        month: Int,
    ) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(dogBirthdayYear = year, dogBirthdayMonth = month)
    }

    fun registerDog() {
        if (isProfileComplete.value == true) {
            viewModelScope.launch {
                val state = uiState.value ?: return@launch
                val name = dogName.value ?: return@launch
                val description = dogDescription.value ?: return@launch
                val birthday = LocalDate(state.dogBirthdayYear, state.dogBirthdayMonth, 1)

                postPetUseCase(
                    name = name,
                    description = description,
                    birthday = birthday,
                    sizeType = dogSize.toSizeType(),
                    gender = dogGender.toGender(state.neutering),
                    imageUrl = "https://docs.api.com",
                ).onSuccess {
                    _navigateAction.emit(RegisterDogNavigationAction.NavigateToMyPage)
                }.onFailure {
                    // TODO 예외 처리
                }
            }
        }
    }

    companion object {
        fun factory(postPetUseCase: PostPetUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterDogViewModel(postPetUseCase = postPetUseCase)
            }
        }
    }
}
