package com.happy.friendogly.presentation.ui.club.select

sealed interface PetSelectEvent {
    data object CancelSelection : PetSelectEvent

    data class PreventSelection(val dogName: String) : PetSelectEvent

    data object SelectPet : PetSelectEvent

    data class SelectPets(val pets: List<Long>) : PetSelectEvent

    data object EmptyPet : PetSelectEvent

    data object PreventCommit : PetSelectEvent
}
