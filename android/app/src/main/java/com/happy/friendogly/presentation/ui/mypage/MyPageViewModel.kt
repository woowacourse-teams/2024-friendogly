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

    init {
        fetchMemberMine()
        fetchPetMine()
    }

    fun fetchMemberMine() {
        viewModelScope.launch {
            getMemberMineUseCase().onSuccess { member ->
                val state = uiState.value ?: return@launch

                _uiState.value =
                    state.copy(
                        id = member.id,
                        nickname = member.name,
                        email = member.email,
                        tag = member.tag,
                        profilePath = member.imageUrl,
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

                _uiState.value = state.copy(pets = petsView + PetAddView(memberId = state.id))
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
        _navigateAction.emit(MyPageNavigationAction.NavigateToProfileEdit)
    }

    fun navigateToSetting() {
        _navigateAction.emit(MyPageNavigationAction.NavigateToSetting)
    }

    override fun navigateToPetEdit(id: Long) {
        _navigateAction.emit(MyPageNavigationAction.NavigateToPetEdit)
    }

    fun navigateToMyParticipation() {
        _navigateAction.emit(MyPageNavigationAction.NavigateToMyParticipation)
    }

    fun navigateToMyClubManger() {
        _navigateAction.emit(MyPageNavigationAction.NavigateToMyClubManger)
    }

    companion object {
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
