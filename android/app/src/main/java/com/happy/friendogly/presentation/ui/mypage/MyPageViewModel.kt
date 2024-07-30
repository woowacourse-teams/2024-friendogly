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
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    private fun fetchMemberMine() {
        viewModelScope.launch {
            getMemberMineUseCase().onSuccess { member ->
                _uiState.value =
                    uiState.value?.copy(
                        nickname = member.name,
                        email = member.email,
                        tag = member.tag,
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

                _uiState.value =
                    uiState.value?.copy(pets = petsView + PetAddView(memberId = pets.first().memberId))
            }.onFailure {
                // TODO 예외 처리
            }
        }
    }

    fun updateCurrentPage(page: Int) {
        _currentPage.value = page
    }

    override fun navigateToDogDetail(id: Long) {
        _navigateAction.emit(MyPageNavigationAction.NavigateToDogDetail(id = id))
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
        val dog =
            Dog(
                name = "땡이",
                description = "강인해요",
                birthDate = LocalDate.now(),
                sizeType = "",
                gender = "",
                isNeutered = true,
                image = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
            )
        val dogs =
            listOf<Dog>(
                dog,
                dog.copy(
                    name = "초코",
                    image = "https://github.com/user-attachments/assets/a344d355-8b00-4e08-a33f-08db58010b07",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
            )

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
