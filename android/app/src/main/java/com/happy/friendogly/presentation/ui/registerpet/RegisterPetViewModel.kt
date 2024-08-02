package com.happy.friendogly.presentation.ui.registerpet

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.PostPetUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.addSourceList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class RegisterPetViewModel(
    private val postPetUseCase: PostPetUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<RegisterPetUiState> =
        MutableLiveData(RegisterPetUiState())
    val uiState: LiveData<RegisterPetUiState> get() = _uiState

    private val _profileImage: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val profileImage: LiveData<Bitmap?> get() = _profileImage

    val petName = MutableLiveData<String>("")
    val petDescription = MutableLiveData<String>("")
    private var petSize: PetSize = PetSize.SMALL
    private var petGender: PetGender = PetGender.MAIL

    val isProfileComplete =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                petName,
                petDescription,
                profileImage,
            ) {
                val name = petName.value ?: return@addSourceList false
                val description = petDescription.value ?: return@addSourceList false

                name.isNotBlank() && description.isNotBlank() && profileImage.value != null
            }
        }

    private val _navigateAction: MutableLiveData<Event<RegisterPetNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterPetNavigationAction>> get() = _navigateAction

    fun executeBackAction() {
        _navigateAction.emit(RegisterPetNavigationAction.NavigateToBack)
    }

    fun selectPetProfileImage() {
        _navigateAction.emit(RegisterPetNavigationAction.NavigateToSetProfileImage)
    }

    fun selectPetBirthday() {
        val state = _uiState.value ?: return

        _navigateAction.emit(
            RegisterPetNavigationAction.NavigateToSetBirthday(
                state.petBirthdayYear,
                state.petBirthdayMonth,
            ),
        )
    }

    fun updatePetProfileImage(bitmap: Bitmap) {
        _profileImage.value = bitmap
    }

    fun updatePetProfileFile(file: MultipartBody.Part) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = file)
    }

    fun updatePetSize(petSize: PetSize) {
        this.petSize = petSize
    }

    fun updatePetGender(petGender: PetGender) {
        this.petGender = petGender
    }

    fun updateNeutering() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(neutering = !state.neutering)
    }

    fun updatePetBirthday(
        year: Int,
        month: Int,
    ) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(petBirthdayYear = year, petBirthdayMonth = month)
    }

    fun registerPet() {
        if (isProfileComplete.value == true) {
            viewModelScope.launch {
                val state = uiState.value ?: return@launch
                val name = petName.value ?: return@launch
                val description = petDescription.value ?: return@launch
                val birthday = LocalDate(state.petBirthdayYear, state.petBirthdayMonth, 1)

                postPetUseCase(
                    name = name,
                    description = description,
                    birthday = birthday,
                    sizeType = petSize.toSizeType(),
                    gender = petGender.toGender(state.neutering),
                    file = state.profilePath,
                ).onSuccess {
                    _navigateAction.emit(RegisterPetNavigationAction.NavigateToMyPage)
                }.onFailure {
                    // TODO 예외 처리
                }
            }
        }
    }

    companion object {
        fun factory(postPetUseCase: PostPetUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterPetViewModel(postPetUseCase = postPetUseCase)
            }
        }
    }
}
