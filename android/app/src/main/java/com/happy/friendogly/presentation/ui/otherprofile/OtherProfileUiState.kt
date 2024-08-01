package com.happy.friendogly.presentation.ui.otherprofile

import com.happy.friendogly.domain.model.Pet

data class OtherPetUiState(
    val nickname: String = "",
    val email: String = "",
    val tag: String = "",
    val profilePath: String? = null,
    val pets: List<Pet> = emptyList(),
)
