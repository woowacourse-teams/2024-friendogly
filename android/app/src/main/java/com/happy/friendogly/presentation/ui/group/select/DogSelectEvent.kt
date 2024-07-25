package com.happy.friendogly.presentation.ui.group.select

sealed interface DogSelectEvent {
    data object CancelSelection : DogSelectEvent

    data class PreventSelection(val dogName: String) : DogSelectEvent

    data object SelectDog : DogSelectEvent

    data class SelectDogs(val dogs: List<Long>) : DogSelectEvent
}
