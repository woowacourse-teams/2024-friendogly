package com.happy.friendogly.data.model

data class ClubPetDto(
    val id: Long,
    val name: String,
    val imageUrl: String?=null,
    val isMine: Boolean,
)
