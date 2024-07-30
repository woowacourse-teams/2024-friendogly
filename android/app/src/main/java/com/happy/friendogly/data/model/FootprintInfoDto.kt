package com.happy.friendogly.data.model

import com.happy.friendogly.domain.model.WalkStatus
import com.happy.friendogly.remote.util.LocalDateSerializer
import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

data class FootprintInfoDto(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    @Serializable(with = LocalDateSerializer::class)
    val petBirthDate: LocalDate,
    val petSizeType: SizeTypeDto,
    val petGender: GenderDto,
    val footprintImageUrl: String,
    val walkStatus: WalkStatus,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startWalkTime: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endWalkTime: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
