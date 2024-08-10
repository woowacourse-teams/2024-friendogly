package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MyFootprintResponse(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)
