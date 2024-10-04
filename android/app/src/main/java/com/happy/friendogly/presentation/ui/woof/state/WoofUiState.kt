package com.happy.friendogly.presentation.ui.woof.state

sealed interface WoofUiState {
    data object Loading : WoofUiState

    data object LocationPermissionsNotGranted : WoofUiState

    data object FindingFriends : WoofUiState

    data object RegisteringPlayground : WoofUiState

    data object ViewingFootprintInfo : WoofUiState
}
