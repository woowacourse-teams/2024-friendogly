package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class ClubDetailDto(
    val id: Long,
    val title: String,
    val content: String,
    val ownerMemberName: String,
    val address: ClubAddressDto,
    val status: ClubStateDto,
    val createdAt: LocalDateTime,
    val allowedGender: List<GenderDto>,
    val allowedSize: List<SizeTypeDto>,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String? = null,
    val ownerImageUrl: String? = null,
    val isMine: Boolean,
    val alreadyParticipate: Boolean,
    val canParticipate: Boolean,
    val isMyPetsEmpty: Boolean,
    val memberDetails: List<ClubMemberDto>,
    val petDetails: List<ClubPetDto>,
    val chatRoomId: Long? = null,
)
