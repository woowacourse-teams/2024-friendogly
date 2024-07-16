package com.woowacourse.friendogly.remote.dto.request

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class RequestPetPostDto(
    val memberId: Long,
    val name: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: String
)
