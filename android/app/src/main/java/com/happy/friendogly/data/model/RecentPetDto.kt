package com.happy.friendogly.data.model

data class RecentPetDto(
    val memberId: Long,
    val imgUrl: String,
    val name: String,
    val id: Long = 0,
)
