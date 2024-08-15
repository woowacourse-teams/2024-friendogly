package com.happy.friendogly.presentation.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class MyPageViewModel(
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

    fun fetchMemberMine() {
        viewModelScope.launch {
            getMemberMineUseCase().onSuccess { member ->
                val state = uiState.value ?: return@launch

                _uiState.value =
                    state.copy(
                        id = member.id,
                        nickname = member.name,
                        tag = member.tag,
                        imageUrl = member.imageUrl,
                    )
            }.onFailure {
                // TODO 예외 처리
            }
        }
    }

    fun fetchPetMine() {
        viewModelScope.launch {
            getPetsMineUseCase().onSuccess { pets ->
                val petsView = pets.map { pet -> PetView.from(pet = pet) }

                val state = uiState.value ?: return@launch

                if (petsView.size >= MAX_PET_SIZE) {
                    _uiState.value = state.copy(pets = petsView)
                } else {
                    _uiState.value = state.copy(pets = petsView + PetAddView(memberId = state.id))
                }
            }.onFailure {
                // TODO 예외 처리
            }
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

    override fun navigateToPetEdit(id: Long) {
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
