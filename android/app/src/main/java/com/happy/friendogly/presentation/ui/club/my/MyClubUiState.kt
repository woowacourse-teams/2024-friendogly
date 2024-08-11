package com.happy.friendogly.presentation.ui.club.my

import com.happy.friendogly.presentation.ui.club.list.ClubListUiState

sealed interface MyClubUiState {
    data object Init : MyClubUiState

    data object NotData : MyClubUiState

    data object Error : MyClubUiState
}
