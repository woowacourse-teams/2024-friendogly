package com.woowacourse.friendogly.presentation.ui.mypage

import com.woowacourse.friendogly.domain.model.Pet
import java.time.LocalDate

data class MyPageUiState(
    val nickname: String = "",
    val email: String = "",
    val profilePath: String? = null,
    val pets: List<Pet> = emptyList(),
)

// TODO 더미 데이터 모델
data class Dog(
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: String,
    val gender: String,
    val isNeutered: Boolean,
    val image: String,
)
