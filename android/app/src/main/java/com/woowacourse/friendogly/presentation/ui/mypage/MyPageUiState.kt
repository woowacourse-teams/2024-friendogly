package com.woowacourse.friendogly.presentation.ui.mypage

import java.time.LocalDate

data class MyPageUiState(
    val nickname: String = "",
    val email: String = "",
    val profilePath: String? = null,
    val dogs: List<Dog> = emptyList(),
)

data class Dog(
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: String,
    val gender: String,
    val isNeutered: Boolean,
    val image: String,
)
