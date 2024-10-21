package com.happy.friendogly.presentation.ui.club.detail

sealed interface ClubDetailUiState {
    data object Loading : ClubDetailUiState

    data object Init : ClubDetailUiState
}
