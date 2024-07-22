package com.woowacourse.friendogly.presentation.ui.group.model

import com.woowacourse.friendogly.presentation.ui.group.list.model.GroupWoof
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import java.time.LocalDateTime

data class GroupUiModel(
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
    val groupReaderImage: String,
    val groupDate: LocalDateTime,
    val groupWoofs: List<GroupWoof>,
)
