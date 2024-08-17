package com.happy.friendogly.presentation.ui.registerpet

sealed interface RegisterPetMessage {
    data object FileSizeExceedMessage : RegisterPetMessage

    data object ServerErrorMessage : RegisterPetMessage
}
