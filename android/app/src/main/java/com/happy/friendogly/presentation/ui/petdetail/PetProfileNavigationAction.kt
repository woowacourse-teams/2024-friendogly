package com.happy.friendogly.presentation.ui.petdetail

sealed interface PetProfileNavigationAction {
    data object NavigateToBack : PetProfileNavigationAction
}
