package com.woowacourse.friendogly.presentation.ui.dogdetail

import com.woowacourse.friendogly.presentation.ui.mypage.Dog

data class DogDetailUiState(
    val dogs: List<Dog> = emptyList(),
)
