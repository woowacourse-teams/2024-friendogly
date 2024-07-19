package com.woowacourse.friendogly.presentation.ui.registerdog

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import okhttp3.MultipartBody

class RegisterDogViewModel : BaseViewModel() {
    private val _uiState: MutableLiveData<RegisterDogUiState> =
        MutableLiveData(RegisterDogUiState())
    val uiState: LiveData<RegisterDogUiState> get() = _uiState

    private val _profileImage: MutableLiveData<Bitmap?> =
        MutableLiveData(null)
    val profileImage: LiveData<Bitmap?> get() = _profileImage

    val dogName = MutableLiveData<String>("")
    val dogDescription = MutableLiveData<String>("")
    private var dogSize: DogSize = DogSize.SMALL
    private var dogGender: DogGender = DogGender.MAIL

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
        val isProfileComplete =
            dogName.value == "" || dogDescription.value == "" || _profileImage.value == null
        if (!isProfileComplete) return
        _navigateAction.emit(RegisterDogNavigationAction.NavigateToMyPage)
    }
}
