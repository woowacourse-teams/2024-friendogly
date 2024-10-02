package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundPetDetailResponse(
    val memberId: Long,
    val petId: Long,
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeResponse,
    val gender: GenderResponse,
    val imageUrl: String,
    val message: String,
    val isArrival: Boolean,
    val isMine: Boolean,
)
