package com.happy.friendogly.presentation.ui.woof

sealed interface WoofUiState {
    data object Loading : WoofUiState

    data object LocationPermissionsNotGranted : WoofUiState

    data class FindingFriends(val refreshBtnVisible: Boolean = false) : WoofUiState

    data object RegisteringFootprint : WoofUiState

    data object ViewingFootprintInfo : WoofUiState
}
