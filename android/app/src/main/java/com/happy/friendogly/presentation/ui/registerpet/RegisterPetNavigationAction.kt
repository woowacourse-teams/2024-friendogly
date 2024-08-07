package com.happy.friendogly.presentation.ui.registerpet

sealed interface RegisterPetNavigationAction {
    data object NavigateToSetProfileImage : RegisterPetNavigationAction

    data class NavigateToSetBirthday(val year: Int, val month: Int) : RegisterPetNavigationAction

    data object NavigateToBack : RegisterPetNavigationAction

    data object NavigateToMyPage : RegisterPetNavigationAction
}
