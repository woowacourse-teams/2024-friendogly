package com.happy.friendogly.presentation.ui.group.list

sealed interface GroupListUiState {
    data object Init : GroupListUiState

    data object NotData : GroupListUiState

    data object NotAddress : GroupListUiState

    data object Error : GroupListUiState
}
