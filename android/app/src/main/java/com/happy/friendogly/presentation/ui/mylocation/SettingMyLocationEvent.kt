package com.happy.friendogly.presentation.ui.mylocation

sealed interface SettingMyLocationEvent {
    sealed interface Navigation: SettingMyLocationEvent {
        data object NavigateToPrev : Navigation
    }
}
