package com.woowacourse.friendogly.presentation.ui.group.list

sealed interface GroupListEvent {
    data class OpenGroup(val groupId: Long) : GroupListEvent
}
