package com.happy.friendogly.presentation.ui.petdetail

import com.happy.friendogly.presentation.ui.mypage.Dog

data class PetDetailUiState(
    val dogs: List<Dog> = emptyList(),
)
