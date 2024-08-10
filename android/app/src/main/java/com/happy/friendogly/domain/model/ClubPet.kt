package com.happy.friendogly.domain.model

data class ClubPet(
    val id: Long,
    val name: String,
    val imageUrl: String? = null,
    val isMine: Boolean,
)
