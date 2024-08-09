package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDateTime

data class ClubDetail(
    val id: Long,
    val title: String,
    val content: String,
    val ownerMemberName: String,
    val address: ClubAddress,
    val status: ClubState,
    val createdAt: LocalDateTime,
    val allowedGender: List<Gender>,
    val allowedSize: List<SizeType>,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String? = null,
    val ownerImageUrl: String? = null,
    val isMine: Boolean,
    val alreadyParticipate: Boolean,
    val canParticipate: Boolean,
    val memberDetails: List<ClubMember>,
    val petDetails: List<ClubPet>,
)
