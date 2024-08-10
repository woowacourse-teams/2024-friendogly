package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDateTime

data class Club(
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
    val petImageUrls: List<String?>,
)
