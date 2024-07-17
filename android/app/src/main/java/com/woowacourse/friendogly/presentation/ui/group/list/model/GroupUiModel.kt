package com.woowacourse.friendogly.presentation.ui.group.list.model

import com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter.GroupFilter
import java.time.LocalDate
import java.time.LocalDateTime

data class GroupUiModel(
    val groupId: Long,
    val filters: List<GroupFilter>,
    val groupPoster: String,
    val isParticipable: String,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val groupLocation: String,
    val groupLeader: String,
    val groupDate: LocalDateTime,
)
