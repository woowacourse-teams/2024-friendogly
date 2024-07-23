package com.woowacourse.friendogly.remote.model.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class PetResponse(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeResponse,
    val gender: GenderResponse,
    val imageUrl: String,
)
