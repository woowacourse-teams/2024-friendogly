package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateSerializer
import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FootprintInfoResponse(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    @Serializable(with = LocalDateSerializer::class)
    val petBirthDate: LocalDate,
    val petSizeType: SizeTypeResponse,
    val petGender: GenderResponse,
    val footprintImageUrl: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
