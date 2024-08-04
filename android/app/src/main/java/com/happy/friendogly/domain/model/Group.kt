package com.happy.friendogly.domain.model

import java.time.LocalDateTime

data class Group(
    val groupId: Long,
    val allowedGender: List<Gender>,
    val allowedSize: List<SizeType>,
    val groupPoster: String,
    val isParticipable: Boolean,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupLeaderImage: String,
    val groupDate: LocalDateTime,
    val groupWoofs: List<GroupPet>,
)
