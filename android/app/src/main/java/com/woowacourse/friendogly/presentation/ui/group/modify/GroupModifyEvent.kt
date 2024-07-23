package com.woowacourse.friendogly.presentation.ui.group.modify

sealed interface GroupModifyEvent {
    data object CancelSelection : GroupModifyEvent

    data object Modify : GroupModifyEvent

    data object Delete : GroupModifyEvent

    data object Report : GroupModifyEvent

    data object Block : GroupModifyEvent
}
