package com.happy.friendogly.presentation.ui.group.detail

import com.happy.friendogly.presentation.ui.group.detail.model.DetailViewType
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import java.time.LocalDateTime

data class GroupDetailUiModel(
    val groupId: Long,
    val filters: List<GroupFilter>,
    val groupPoster: String?=null,
    val detailViewType: DetailViewType,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupReaderImage: String?=null,
    val groupDate: LocalDateTime,
    val userProfiles: List<GroupDetailProfileUiModel>,
    val dogProfiles: List<GroupDetailProfileUiModel>,
)
