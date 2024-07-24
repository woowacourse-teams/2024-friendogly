package com.woowacourse.friendogly.remote.model.request

import com.woowacourse.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PostPetRequest(
    val name: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeRequest,
    val gender: GenderRequest,
)
