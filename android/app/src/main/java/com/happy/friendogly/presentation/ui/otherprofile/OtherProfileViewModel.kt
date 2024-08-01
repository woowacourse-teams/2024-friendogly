package com.happy.friendogly.presentation.ui.otherprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetMemberMineUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import kotlinx.coroutines.launch

class OtherProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPetsMineUseCase: GetPetsMineUseCase,
    private val getMemberMineUseCase: GetMemberMineUseCase,
) : BaseViewModel(), OtherProfileActionHandler {
    init {
        val userId =
            requireNotNull(savedStateHandle.get<Long>(OtherProfileActivity.PUT_EXTRA_USER_ID))
    }

    private val _uiState: MutableLiveData<OtherProfileUiState> = MutableLiveData(OtherProfileUiState())
    val uiState: LiveData<OtherProfileUiState> get() = _uiState

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> get() = _currentPage

    private val _navigateAction: MutableLiveData<Event<OtherProfileNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<OtherProfileNavigationAction>> get() = _navigateAction

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

    override fun navigateToPetDetail(id: Long) {
        _navigateAction.emit(OtherProfileNavigationAction.NavigateToPetDetail(id = id))
    }

    fun navigateToMore(id: Long) {
        _navigateAction.emit(OtherProfileNavigationAction.NavigateToUserMore(id = id))
    }

    companion object {
        fun factory(
            getPetsMineUseCase: GetPetsMineUseCase,
            getMemberMineUseCase: GetMemberMineUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { creator ->
                OtherProfileViewModel(
                    savedStateHandle = creator.createSavedStateHandle(),
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )
            }
        }
    }
}
