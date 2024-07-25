package com.happy.friendogly.presentation.ui.group.select

interface DogSelectActionHandler {
    fun selectDog(dogSelectUiModel: DogSelectUiModel)

    fun submitDogs()

    fun cancelSelection()
}
