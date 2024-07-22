package com.woowacourse.friendogly.presentation.ui.group.detail

import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

sealed interface GroupDetailEvent {
    data class OpenDogSelector(val filters: List<GroupFilter>): GroupDetailEvent
    data object JoinGroup : GroupDetailEvent
}
