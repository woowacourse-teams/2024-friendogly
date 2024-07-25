package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PetResponse(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeResponse,
    val gender: GenderResponse,
    val imageUrl: String,
)
