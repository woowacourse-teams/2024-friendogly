package com.happy.friendogly.presentation.ui.otherprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetMemberUseCase
import com.happy.friendogly.domain.usecase.GetPetsUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.petdetail.PetDetail
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import kotlinx.coroutines.launch

class OtherProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPetsUseCase: GetPetsUseCase,
    private val getMemberUseCase: GetMemberUseCase,
) : BaseViewModel(), OtherProfileActionHandler {
    val id = requireNotNull(savedStateHandle.get<Long>(OtherProfileActivity.PUT_EXTRA_USER_ID))

    private val _uiState: MutableLiveData<OtherProfileUiState> =
        MutableLiveData(OtherProfileUiState())
    val uiState: LiveData<OtherProfileUiState> get() = _uiState

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> get() = _currentPage

    private val _navigateAction: MutableLiveData<Event<OtherProfileNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<OtherProfileNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<OtherProfileMessage>> = MutableLiveData(null)
    val message: LiveData<Event<OtherProfileMessage>> get() = _message

    init {
        fetchMember()
        fetchPetMine()
    }

    private fun fetchMember() {
        launch {
            getMemberUseCase(id = id).fold(
                onSuccess = { member ->
                    val state = uiState.value ?: return@launch
                    _uiState.value =
                        state.copy(
                            nickname = member.name,
                            tag = member.tag,
                            profilePath = member.imageUrl,
                        )
                },
                onError = {
                    _message.emit(OtherProfileMessage.DefaultErrorMessage)
                },
            )
        }
    }

    fun fetchPetMine() {
        viewModelScope.launch {
            getPetsUseCase(id = id).onSuccess { pets ->
                _uiState.value =
                    uiState.value?.copy(pets = pets)
            }.onFailure {
                // TODO 예외 처리
            }
        }
    }

    fun updateCurrentPage(page: Int) {
        _currentPage.value = page
    }

    override fun navigateToBack() {
        _navigateAction.emit(OtherProfileNavigationAction.NavigateToBack)
    }

    override fun navigateToPetDetail() {
        val state = uiState.value ?: return
        val currentPage = currentPage.value ?: return

        val petDetail =
            state.pets.map { pet ->
                PetDetail(
                    id = pet.id,
                    name = pet.name,
                    description = pet.description,
                    birthDate = pet.birthDate,
                    sizeType = pet.sizeType,
                    gender = pet.gender,
                    imageUrl = pet.imageUrl,
                )
            }
        val petsDetail = PetsDetail(petDetail)

        _navigateAction.emit(
            OtherProfileNavigationAction.NavigateToPetDetail(
                currentPage = currentPage,
                petsDetail = petsDetail,
            ),
        )
    }

    fun navigateToMore(id: Long) {
        _navigateAction.emit(OtherProfileNavigationAction.NavigateToUserMore(id = id))
    }

    companion object {
        fun factory(
            getPetsUseCase: GetPetsUseCase,
            getMemberUseCase: GetMemberUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { creator ->
                OtherProfileViewModel(
                    savedStateHandle = creator.createSavedStateHandle(),
                    getPetsUseCase = getPetsUseCase,
                    getMemberUseCase = getMemberUseCase,
                )
            }
        }
    }
}
