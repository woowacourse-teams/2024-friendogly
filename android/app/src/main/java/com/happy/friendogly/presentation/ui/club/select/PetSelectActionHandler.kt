package com.happy.friendogly.presentation.ui.club.select

interface PetSelectActionHandler {
    fun selectPet(petSelectUiModel: PetSelectUiModel)

    fun submitDogs()

    fun cancelSelection()
}
