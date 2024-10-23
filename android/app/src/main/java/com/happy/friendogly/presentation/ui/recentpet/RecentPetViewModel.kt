package com.happy.friendogly.presentation.ui.recentpet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.domain.usecase.GetAllRecentPetUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.recentpet.adapter.RecentPetListSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentPetViewModel
    @Inject
    constructor(
        getAllRecentPetUseCase: GetAllRecentPetUseCase,
    ) : BaseViewModel(), RecentPetActionHandler {
        private val _uiState: MutableLiveData<RecentPetUiState> = MutableLiveData(RecentPetUiState())
        val uiState: LiveData<RecentPetUiState> get() = _uiState

        private val recentPetListSource =
            RecentPetListSource(getAllRecentPetUseCase = getAllRecentPetUseCase)

        private val _navigateAction: MutableLiveData<Event<RecentPetNavigationAction>> =
            MutableLiveData(null)
        val navigateAction: LiveData<Event<RecentPetNavigationAction>> get() = _navigateAction

        init {
            getAllRecentPet()
        }

        private fun getAllRecentPet() {
            launch {
                val state = _uiState.value ?: return@launch
                val items = recentPetListSource.load()
                _uiState.value = state.copy(recentPets = items)
            }
        }

        fun navigateToBack() {
            _navigateAction.emit(RecentPetNavigationAction.NavigateToBack)
        }

        override fun navigateToProfile(memberId: Long) {
            _navigateAction.emit(RecentPetNavigationAction.NavigateToOtherProfile(memberId = memberId))
        }

        override fun navigateToPetImage(petImageUrl: String) {
            _navigateAction.emit(RecentPetNavigationAction.NavigateToPetImage(petImageUrl = petImageUrl))
        }
    }
