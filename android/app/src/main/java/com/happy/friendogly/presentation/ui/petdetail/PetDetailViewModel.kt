package com.happy.friendogly.presentation.ui.petdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.getSerializable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel() {
        private val _uiState: MutableLiveData<PetDetailUiState> = MutableLiveData(PetDetailUiState())
        val uiState: LiveData<PetDetailUiState> get() = _uiState

        private val _currentPage: MutableLiveData<Int> = MutableLiveData(MIDDLE_PAGE)
        val currentPage: LiveData<Int> get() = _currentPage

        private val _navigateAction: MutableLiveData<Event<PetProfileNavigationAction>> =
            MutableLiveData(null)
        val navigateAction: LiveData<Event<PetProfileNavigationAction>> get() = _navigateAction

        init {
            fetchPetsDetail()
        }

        private fun fetchPetsDetail() {
            val petsDetail =
                requireNotNull(
                    savedStateHandle.getSerializable(
                        PetDetailActivity.PUT_EXTRA_PETS_DETAIL,
                        PetsDetail.serializer(),
                    ),
                )

            val currentPage =
                requireNotNull(savedStateHandle.get<Int>(PetDetailActivity.PUT_EXTRA_CURRENT_PAGE))

            val state = _uiState.value ?: return
            _uiState.value = state.copy(petsDetail = petsDetail, startPage = currentPage)
            _currentPage.value = currentPage
        }

        fun updateCurrentPage(page: Int) {
            _currentPage.value = page
        }

        fun navigateToBack() {
            _navigateAction.emit(PetProfileNavigationAction.NavigateToBack)
        }

        companion object {
            private const val MIDDLE_PAGE = Int.MAX_VALUE / 2
        }
    }
