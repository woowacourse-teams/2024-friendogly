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
    val address: ClubAddressResponse,
    val status: ClubStateResponse,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val allowedGender: List<GenderResponse>,
    val allowedSize: List<SizeTypeResponse>,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String? = null,
    val ownerImageUrl: String? = null,
    val isMine: Boolean,
    val alreadyParticipate: Boolean,
    val canParticipate: Boolean,
    val isMyPetsEmpty: Boolean,
    val memberDetails: List<ClubDetailMemberResponse>,
    val petDetails: List<ClubDetailPetResponse>,
    val chatRoomId: Long? = null,
)
