package com.happy.friendogly.presentation.ui.playground.state

sealed interface PlaygroundUiState {
    data object Loading : PlaygroundUiState

    data object LocationPermissionsNotGranted : PlaygroundUiState

    data object FindingPlayground : PlaygroundUiState

    data object RegisteringPlayground : PlaygroundUiState

    data object ViewingPlaygroundSummary : PlaygroundUiState

    data object ViewingPlaygroundInfo : PlaygroundUiState
}
