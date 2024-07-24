package com.woowacourse.friendogly.data.model

import com.woowacourse.friendogly.remote.util.LocalDateSerializer
import com.woowacourse.friendogly.remote.util.LocalDateTimeSerializer
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
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
