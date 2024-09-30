package com.happy.friendogly.domain.model

data class RecentPet(
    val memberId: Long,
    val imgUrl: String,
    val name: String,
    val id: Long = 0,
)
