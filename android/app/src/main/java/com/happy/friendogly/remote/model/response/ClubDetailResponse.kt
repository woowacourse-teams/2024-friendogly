package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ClubDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val ownerMemberName: String,
    val address: String,
    val status: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val allowedGender: List<GenderResponse>,
    val allowedSize: List<SizeTypeResponse>,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String,
    val ownerImageUrl: String,
    val isMine: Boolean,
    val alreadyParticipate: Boolean,
    val canParticipate: Boolean,
    val memberDetails: List<ClubMemberDetailResponse>,
    val petDetails: List<ClubPetDetailResponse>,
)
