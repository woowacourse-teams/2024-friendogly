package com.happy.friendogly.presentation.ui.group.list

import com.happy.friendogly.presentation.ui.group.list.model.GroupWoof
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.datetime.LocalDateTime

data class GroupListUiModel(
    val groupId: Long,
    val filters: List<GroupFilter>,
    val groupPoster: String,
    val isParticipable: Boolean,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupDate: LocalDateTime,
    val groupWoofs: List<GroupWoof>,
)
