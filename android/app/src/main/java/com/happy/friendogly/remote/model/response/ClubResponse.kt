package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ClubResponse(
    val id: Long,
    val title: String,
    val content: String,
    val ownerMemberName: String,
    val address: ClubAddressResponse,
    val status: ClubStateResponse,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String? = null,
    val petImageUrls: List<String?>,
)
