package com.happy.friendogly.presentation.ui.club.my

sealed interface MyClubUiState {
    data object Init : MyClubUiState

    data object NotData : MyClubUiState

    data object Error : MyClubUiState
}
