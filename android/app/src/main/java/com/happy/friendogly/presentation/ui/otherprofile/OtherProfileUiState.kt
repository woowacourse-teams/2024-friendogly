package com.happy.friendogly.presentation.ui.otherprofile

import com.happy.friendogly.domain.model.Pet

data class OtherProfileUiState(
    val nickname: String = "",
    val tag: String = "",
    val profilePath: String? = null,
    val pets: List<Pet> = emptyList(),
)
