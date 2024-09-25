package com.happy.friendogly.presentation.ui.otherprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetMemberUseCase
import com.happy.friendogly.domain.usecase.GetPetsUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.petdetail.PetDetail
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel
    @Inject
    constructor(
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
                                otherProfileSkeleton = state.otherProfileSkeleton.copy(userProfile = false),
                            )
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_INTERNET -> _message.emit(OtherProfileMessage.NoInternetMessage)
                            DataError.Network.SERVER_ERROR -> _message.emit(OtherProfileMessage.ServerErrorMessage)
                            else -> _message.emit(OtherProfileMessage.ServerErrorMessage)
                        }
                    },
                )
            }
        }

        fun fetchPetMine() {
            launch {
                getPetsUseCase(id = id).fold(
                    onSuccess = { pets ->
                        val state = uiState.value ?: return@launch
                        _uiState.value =
                            state.copy(
                                pets = pets,
                                otherProfileSkeleton = state.otherProfileSkeleton.copy(petProfile = false),
                            )
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_INTERNET -> _message.emit(OtherProfileMessage.NoInternetMessage)
                            DataError.Network.SERVER_ERROR -> _message.emit(OtherProfileMessage.ServerErrorMessage)
                            else -> _message.emit(OtherProfileMessage.ServerErrorMessage)
                        }
                    },
                )
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
    }
