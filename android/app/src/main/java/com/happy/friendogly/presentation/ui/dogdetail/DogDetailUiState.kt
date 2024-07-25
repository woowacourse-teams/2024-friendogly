package com.happy.friendogly.presentation.ui.dogdetail

import com.happy.friendogly.presentation.ui.mypage.Dog

data class DogDetailUiState(
    val dogs: List<Dog> = emptyList(),
)
