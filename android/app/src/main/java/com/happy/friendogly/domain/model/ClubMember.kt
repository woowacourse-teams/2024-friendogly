package com.happy.friendogly.domain.model

data class ClubMember(
    val id: Long,
    val name: String,
    val imageUrl: String? = null,
)
