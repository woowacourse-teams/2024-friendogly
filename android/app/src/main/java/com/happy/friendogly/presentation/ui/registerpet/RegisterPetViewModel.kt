package com.happy.friendogly.presentation.ui.registerpet

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
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

    private var profilePath: MultipartBody.Part? = null
    var profileImageUrl: String? = null

    private var beforePetProfile: PetProfile? = null
    val petName = MutableLiveData<String>("")
    val petDescription = MutableLiveData<String>("")
    val neutering = MutableLiveData(true)
    val petSize = MutableLiveData(PetSize.SMALL)
    val petGender = MutableLiveData(PetGender.MAIL)

    private val _navigateAction: MutableLiveData<Event<RegisterPetNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterPetNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<RegisterPetMessage>> =
        MutableLiveData(null)
    val message: LiveData<Event<RegisterPetMessage>> get() = _message

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData(null)
    val loading: LiveData<Event<Boolean>> get() = _loading

    val isProfileComplete =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                petName,
                petDescription,
                petSize,
                petGender,
                uiState,
                neutering,
            ) {
                isProfileUpdateNeeded()
            }
        }

    private fun isProfileUpdateNeeded(): Boolean {
        val state = uiState.value ?: return false
        val petName = petName.value ?: return false
        val petDescription = petDescription.value ?: return false
        val petSize = petSize.value ?: return false
        val petGender = petGender.value ?: return false
        val neutering = neutering.value ?: return true

        return isPetProfileDifferent(
            state,
            petName,
            petDescription,
            petSize,
            petGender,
            neutering,
        )
    }

    private fun isPetProfileDifferent(
        state: RegisterPetUiState,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
        neutering: Boolean,
    ): Boolean {
        return if (state.isFirstTimeSetup) {
            isFirstTimeSetupProfileDifferent(
                state,
                petName,
                petDescription,
                petSize,
                petGender,
                neutering,
            )
        } else {
            isExistingProfileDifferent(
                state,
                petName,
                petDescription,
                petSize,
                petGender,
                neutering,
            )
        }
    }

    private fun isFirstTimeSetupProfileDifferent(
        state: RegisterPetUiState,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
        neutering: Boolean,
    ): Boolean {
        return (
            (profileImageUrl == null && profilePath != null) &&
                (petName != beforePetProfile?.name && petName.isNotBlank()) &&
                (petDescription != beforePetProfile?.description && petDescription.isNotBlank()) &&
                (petSize.toSizeType() != beforePetProfile?.sizeType) &&
                (petGender.toGender(neutering) != beforePetProfile?.gender) &&
                (
                    (state.petBirthdayYear != beforePetProfile?.birthDate?.year) ||
                        (state.petBirthdayMonth != beforePetProfile?.birthDate?.month?.value)
                )
        )
    }

    private fun isExistingProfileDifferent(
        state: RegisterPetUiState,
        petName: String,
        petDescription: String,
        petSize: PetSize,
        petGender: PetGender,
        neutering: Boolean,
    ): Boolean {
        return (
            (profileImageUrl == null && profilePath != null) ||
                (petName != beforePetProfile?.name && petName.isNotBlank()) ||
                (petDescription != beforePetProfile?.description && petDescription.isNotBlank()) ||
                (petSize.toSizeType() != beforePetProfile?.sizeType) ||
                (petGender.toGender(neutering) != beforePetProfile?.gender) ||
                (
                    (state.petBirthdayYear != beforePetProfile?.birthDate?.year) ||
                        (state.petBirthdayMonth != beforePetProfile?.birthDate?.month?.value)
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
        neutering.value = petProfile.gender.isNeutered()
        beforePetProfile = petProfile
        profileImageUrl = petProfile.imageUrl

        _uiState.value =
            state.copy(
                isFirstTimeSetup = false,
                petBirthdayYear = petProfile.birthDate.year,
                petBirthdayMonth = petProfile.birthDate.monthNumber,
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
        profilePath = file
        profileImageUrl = null
    }

    fun updatePetSize(petSize: PetSize) {
        this.petSize.value = petSize
    }

    fun updatePetGender(petGender: PetGender) {
        this.petGender.value = petGender
    }

    fun updateNeutering() {
        val value = neutering.value ?: return
        neutering.value = !value
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
            launch {
                _loading.emit(true)
                val state = uiState.value ?: return@launch
                val name = petName.value ?: return@launch
                val description = petDescription.value ?: return@launch
                val birthday = LocalDate(state.petBirthdayYear, state.petBirthdayMonth, 1)
                val neutering = neutering.value ?: return@launch

                if (state.isFirstTimeSetup) {
                    postPet(name, description, birthday, neutering, profilePath)
                } else {
                    patchPet(name, description, birthday, neutering, profilePath)
                }
                _loading.emit(false)
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
        ).fold(
            onSuccess = {
                _navigateAction.emit(RegisterPetNavigationAction.NavigateToMyPage)
            },
            onError = { error ->
                when (error) {
                    DataError.Network.FILE_SIZE_EXCEED -> _message.emit(RegisterPetMessage.FileSizeExceedMessage)
                    else -> _message.emit(RegisterPetMessage.ServerErrorMessage)
                }
            },
        )
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
