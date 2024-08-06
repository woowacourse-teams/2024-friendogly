package com.happy.friendogly.presentation.ui.group.detail

import com.happy.friendogly.domain.mapper.toPresentation
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyUiModel
import kotlinx.datetime.LocalDateTime

data class GroupDetailUiModel(
    val groupId: Long,
    val filters: List<GroupFilter>,
    val groupPoster: String? = null,
    val groupDetailViewType: GroupDetailViewType,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupLeaderImage: String? = null,
    val groupDate: LocalDateTime,
    val userProfiles: List<GroupDetailProfileUiModel>,
    val dogProfiles: List<GroupDetailProfileUiModel>,
) {
    fun toGroupModifyUiModel(): GroupModifyUiModel {
        return GroupModifyUiModel(
            title = title,
            content = content,
            groupPoster = groupPoster,
        )
    }
}
