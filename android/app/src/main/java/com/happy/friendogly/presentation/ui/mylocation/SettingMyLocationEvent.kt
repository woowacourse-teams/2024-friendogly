package com.happy.friendogly.presentation.ui.mylocation

sealed interface SettingMyLocationEvent {
    data object InvalidLocation : SettingMyLocationEvent

    sealed interface Navigation : SettingMyLocationEvent {
        data object NavigateToPrev : Navigation
        data object NavigateToPrevWithReload: Navigation
    }
}
