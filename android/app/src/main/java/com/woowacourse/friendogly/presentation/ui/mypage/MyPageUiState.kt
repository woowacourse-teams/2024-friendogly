package com.woowacourse.friendogly.presentation.ui.mypage

import com.woowacourse.friendogly.remote.dto.response.ResponsePetGetDto
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

fun List<ResponsePetGetDto>.toDog(): List<Dog> {
    return this.map {
        it.toDog()
    }
}

fun ResponsePetGetDto.toDog(): Dog {
    return Dog(
        name = this.name,
        description = this.description,
        birthDate = this.birthDate,
        sizeType = this.sizeType,
        gender = this.gender,
        isNeutered = this.isNeutered,
        image = this.image,
    )
}
