package com.happy.friendogly.presentation.ui.registerpet

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.PostPetUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import com.happy.friendogly.presentation.utils.addSourceList
import com.happy.friendogly.presentation.utils.getSerializable
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class RegisterPetViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val postPetUseCase: PostPetUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<RegisterPetUiState> =
        MutableLiveData(RegisterPetUiState())
    val uiState: LiveData<RegisterPetUiState> get() = _uiState

    private val _profileImage: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val profileImage: LiveData<Bitmap?> get() = _profileImage

    val petName = MutableLiveData<String>("")
    val petDescription = MutableLiveData<String>("")
    private val petSize = MutableLiveData(PetSize.SMALL)
    private val petGender = MutableLiveData(PetGender.MAIL)

    private val _navigateAction: MutableLiveData<Event<RegisterPetNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterPetNavigationAction>> get() = _navigateAction

    val isProfileComplete =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                petName,
                petDescription,
                petSize,
                petGender,
                uiState,
            ) {
                isProfileUpdateNeeded()
            }
        }

    private fun isProfileUpdateNeeded(): Boolean {
        val state = uiState.value ?: return false
        val beforePetProfile = state.beforePetProfile
        val petName = petName.value ?: return false
        val petDescription = petDescription.value ?: return false
        val petSize = petSize.value ?: return false
        val petGender = petGender.value ?: return false

        return isPetProfileDifferent(
            state,
            beforePetProfile,
            petName,
            petDescription,
            petSize,
            petGender,
        )
    }

    private fun isPetProfileDifferent(
        state: RegisterPetUiState,
        beforePetProfile: PetProfile?,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
    ): Boolean {
        return if (state.isFirstTimeSetup) {
            isFirstTimeSetupProfileDifferent(
                state,
                beforePetProfile,
                petName,
                petDescription,
                petSize,
                petGender,
            )
        } else {
            isExistingProfileDifferent(
                state,
                beforePetProfile,
                petName,
                petDescription,
                petSize,
                petGender,
            )
        }
    }

    private fun isFirstTimeSetupProfileDifferent(
        state: RegisterPetUiState,
        beforePetProfile: PetProfile?,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
    ): Boolean {
        return (
            (state.profileImageUrl == null && state.profilePath != null) &&
                (petName != beforePetProfile?.name && petName.isNotBlank()) &&
                (petDescription != beforePetProfile?.description && petDescription.isNotBlank()) &&
                (petSize.toSizeType() != beforePetProfile?.sizeType) &&
                (petGender.toGender(state.neutering) != beforePetProfile?.gender) &&
                (
                    (state.petBirthdayYear != beforePetProfile?.birthDate?.year) ||
                        (state.petBirthdayMonth != beforePetProfile.birthDate.month.value)
                )
        )
    }

    private fun isExistingProfileDifferent(
        state: RegisterPetUiState,
        beforePetProfile: PetProfile?,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
    ): Boolean {
        return (
            (state.profileImageUrl == null && state.profilePath != null) ||
                (petName != beforePetProfile?.name && petName.isNotBlank()) ||
                (petDescription != beforePetProfile?.description && petDescription.isNotBlank()) ||
                (petSize.toSizeType() != beforePetProfile?.sizeType) ||
                (petGender.toGender(state.neutering) != beforePetProfile.gender) ||
                (
                    (state.petBirthdayYear != beforePetProfile.birthDate.year) ||
                        (state.petBirthdayMonth != beforePetProfile.birthDate.month.value)
                )
        )
    }

    init {
        fetchPetProfile()
    }

    private fun fetchPetProfile() {
        val petProfile =
            savedStateHandle.getSerializable(
                RegisterPetActivity.PUT_EXTRA_PET_PROFILE,
                PetProfile.serializer(),
            )
        petProfile ?: return

        val state = uiState.value ?: return

        petName.value = petProfile.name
        petDescription.value = petProfile.description
        petGender.value = petProfile.gender.toPetGender()
        petSize.value = petProfile.sizeType.toPetSize()

        _uiState.value =
            state.copy(
                isFirstTimeSetup = false,
                beforePetProfile = petProfile,
                profileImageUrl = petProfile.imageUrl,
                petBirthdayYear = petProfile.birthDate.year,
                petBirthdayMonth = petProfile.birthDate.monthNumber,
                neutering = petProfile.gender.isNeutered(),
            )
    }

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
        _uiState.value = state.copy(profilePath = file, profileImageUrl = null)
    }

    fun updatePetSize(petSize: PetSize) {
        this.petSize.value = petSize
    }

    fun updatePetGender(petGender: PetGender) {
        this.petGender.value = petGender
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
        if (this.isProfileComplete.value == true) {
            viewModelScope.launch {
                val state = uiState.value ?: return@launch
                val name = petName.value ?: return@launch
                val description = petDescription.value ?: return@launch
                val birthday = LocalDate(state.petBirthdayYear, state.petBirthdayMonth, 1)
                val neutering = state.neutering
                val profilePath = state.profilePath

                if (state.isFirstTimeSetup) {
                    postPet(name, description, birthday, neutering, profilePath)
                } else {
                    patchPet(name, description, birthday, neutering, profilePath)
                }
            }
        }
    }

    private suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        neutering: Boolean,
        profilePath: MultipartBody.Part?,
    ) {
        postPetUseCase(
            name = name,
            description = description,
            birthday = birthday,
            sizeType = petSize.value!!.toSizeType(),
            gender = petGender.value!!.toGender(neutering),
            file = profilePath,
        ).onSuccess {
            _navigateAction.emit(RegisterPetNavigationAction.NavigateToMyPage)
        }.onFailure {
            // TODO 예외 처리
        }
    }

    private suspend fun patchPet(
        name: String,
        description: String,
        birthday: LocalDate,
        neutering: Boolean,
        profilePath: MultipartBody.Part?,
    ) {
        // TODO patch use case 호출 예정
    }

    companion object {
        fun factory(postPetUseCase: PostPetUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { creator ->
                RegisterPetViewModel(
                    savedStateHandle = creator.createSavedStateHandle(),
                    postPetUseCase = postPetUseCase,
                )
            }
        }
    }
}
