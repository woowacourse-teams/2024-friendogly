package com.happy.friendogly.presentation.ui.club.my

sealed interface MyClubEvent {
    sealed interface Navigation : MyClubEvent {
        data class NavigateToClub(val clubId: Long) : Navigation

        data object NavigateToAddClub : Navigation
    }

    sealed interface AddPet : MyClubEvent {
        data object OpenAddPet: AddPet
        data object OpenAddClub: AddPet
    }
}
