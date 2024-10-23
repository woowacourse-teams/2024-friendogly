package com.happy.friendogly.presentation.ui.club.list

sealed interface ClubListUiState {
    data object Init : ClubListUiState

    data object Loading : ClubListUiState

    data object NotData : ClubListUiState

    data object NotAddress : ClubListUiState

    data object Error : ClubListUiState
}
