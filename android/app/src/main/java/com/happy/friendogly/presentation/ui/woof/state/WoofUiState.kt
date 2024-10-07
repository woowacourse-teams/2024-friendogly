package com.happy.friendogly.presentation.ui.woof.state

sealed interface WoofUiState {
    data object Loading : WoofUiState

    data object LocationPermissionsNotGranted : WoofUiState

    data object FindingPlayground : WoofUiState

    data object RegisteringPlayground : WoofUiState

    data object ViewingPlaygroundSummary : WoofUiState

    data object ViewingPlaygroundInfo : WoofUiState
}
