package com.happy.friendogly.domain.model

import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel
import java.time.LocalDateTime

data class GroupDetail(
    val id: Long,
    val title: String,
    val content: String,
    val allowedGender: List<Gender>,
    val allowedSize: List<SizeType>,
    val groupPoster: String,
    val groupDetailViewType: GroupDetailViewType,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupLeaderImage: String,
    val groupDate: LocalDateTime,
    val memberDetails: List<GroupMember>,
    val petDetails: List<GroupPet>,
)
