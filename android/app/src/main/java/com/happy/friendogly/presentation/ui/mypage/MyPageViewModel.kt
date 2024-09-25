package com.happy.friendogly.presentation.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetMemberMineUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.petdetail.PetDetail
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val getPetsMineUseCase: GetPetsMineUseCase,
        private val getMemberMineUseCase: GetMemberMineUseCase,
    ) : BaseViewModel(), MyPageActionHandler {
        private val _uiState: MutableLiveData<MyPageUiState> = MutableLiveData(MyPageUiState())
        val uiState: LiveData<MyPageUiState> get() = _uiState

        private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
        val currentPage: LiveData<Int> get() = _currentPage

        private val _navigateAction: MutableLiveData<Event<MyPageNavigationAction>> =
            MutableLiveData(null)
        val navigateAction: LiveData<Event<MyPageNavigationAction>> get() = _navigateAction

        private val _message: MutableLiveData<Event<MyPageMessage>> = MutableLiveData(null)
        val message: LiveData<Event<MyPageMessage>> get() = _message

        init {
            fetchMemberMine()
            fetchPetMine()
        }

        fun fetchMemberMine() {
            launch {
                getMemberMineUseCase().fold(
                    onSuccess = { member ->
                        val state = uiState.value ?: return@launch

                        _uiState.value =
                            state.copy(
                                id = member.id,
                                nickname = member.name,
                                tag = member.tag,
                                imageUrl = member.imageUrl,
                                myPageSkeleton = state.myPageSkeleton.copy(userProfile = false),
                            )
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_INTERNET -> _message.emit(MyPageMessage.NoInternetMessage)
                            DataError.Network.SERVER_ERROR -> _message.emit(MyPageMessage.ServerErrorMessage)
                            else -> _message.emit(MyPageMessage.ServerErrorMessage)
                        }
                    },
                )
            }
        }

        fun fetchPetMine() {
            launch {
                getPetsMineUseCase().fold(
                    onSuccess = { pets ->
                        val state = uiState.value ?: return@launch
                        val petsView = pets.map { pet -> PetView.from(pet = pet) }

                        if (petsView.size >= MAX_PET_SIZE) {
                            _uiState.value =
                                state.copy(
                                    pets = petsView,
                                    myPageSkeleton = state.myPageSkeleton.copy(petProfile = false),
                                )
                        } else {
                            _uiState.value =
                                state.copy(
                                    pets = petsView + PetAddView(memberId = state.id),
                                    myPageSkeleton = state.myPageSkeleton.copy(petProfile = false),
                                )
                        }
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_INTERNET -> _message.emit(MyPageMessage.NoInternetMessage)
                            DataError.Network.SERVER_ERROR -> _message.emit(MyPageMessage.ServerErrorMessage)
                            else -> _message.emit(MyPageMessage.ServerErrorMessage)
                        }
                    },
                )
            }
        }

        fun updateCurrentPage(page: Int) {
            _currentPage.value = page
        }

        override fun navigateToPetDetail() {
            val state = uiState.value ?: return
            val currentPage = currentPage.value ?: return

            val petDetail =
                state.pets.filterIsInstance<PetView>().map { petView ->
                    PetDetail(
                        id = petView.id,
                        name = petView.name,
                        description = petView.description,
                        birthDate = petView.birthDate,
                        sizeType = petView.sizeType,
                        gender = petView.gender,
                        imageUrl = petView.imageUrl,
                    )
                }
            val petsDetail = PetsDetail(petDetail)

            _navigateAction.emit(
                MyPageNavigationAction.NavigateToPetDetail(
                    currentPage = currentPage,
                    petsDetail = petsDetail,
                ),
            )
        }

        override fun navigateToRegisterDog(id: Long) {
            _navigateAction.emit(MyPageNavigationAction.NavigateToDogRegister)
        }

        override fun navigateToProfileEdit() {
            val state = uiState.value ?: return
            val profile =
                Profile(
                    name = state.nickname,
                    imageUrl = state.imageUrl,
                )
            _navigateAction.emit(MyPageNavigationAction.NavigateToProfileEdit(profile = profile))
        }

        fun navigateToSetting() {
            _navigateAction.emit(MyPageNavigationAction.NavigateToSetting)
        }

        override fun navigateToPetEdit() {
            val state = uiState.value ?: return
            val currentPage = currentPage.value ?: return
            val pet = state.pets[currentPage] as PetView

            val petProfile =
                PetProfile(
                    id = pet.id,
                    name = pet.name,
                    description = pet.description,
                    birthDate = pet.birthDate,
                    sizeType = pet.sizeType,
                    gender = pet.gender,
                    imageUrl = pet.imageUrl,
                )

            _navigateAction.emit(MyPageNavigationAction.NavigateToPetEdit(petProfile))
        }

        fun navigateToMyParticipation() {
            _navigateAction.emit(MyPageNavigationAction.NavigateToMyParticipation)
        }

        fun navigateToMyClubManger() {
            _navigateAction.emit(MyPageNavigationAction.NavigateToMyClubManger)
        }

        companion object {
            const val MAX_PET_SIZE = 5

            fun factory(
                getPetsMineUseCase: GetPetsMineUseCase,
                getMemberMineUseCase: GetMemberMineUseCase,
            ): ViewModelProvider.Factory {
                return BaseViewModelFactory { _ ->
                    MyPageViewModel(
                        getPetsMineUseCase = getPetsMineUseCase,
                        getMemberMineUseCase = getMemberMineUseCase,
                    )
                }
            }
        }
    }
