package com.woowacourse.friendogly.data.model

import com.woowacourse.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

data class PetDto(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeDto,
    val gender: GenderDto,
    val imageUrl: String,
)
