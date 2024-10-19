package com.happy.friendogly.presentation.ui.playground.state

import com.naver.maps.map.overlay.CircleOverlay

sealed interface PlaygroundUiState {
    data object Loading : PlaygroundUiState

    data object LocationPermissionsNotGranted : PlaygroundUiState

    data object FindingPlayground : PlaygroundUiState

    data class RegisteringPlayground(
        val circleOverlay: CircleOverlay = CircleOverlay(),
        var address: String? = null,
    ) : PlaygroundUiState

    data object ViewingPlaygroundSummary : PlaygroundUiState

    data object ViewingPlaygroundInfo : PlaygroundUiState
}
