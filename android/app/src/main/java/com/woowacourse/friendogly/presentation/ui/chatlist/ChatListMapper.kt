package com.woowacourse.friendogly.presentation.ui.chatlist

import com.woowacourse.friendogly.presentation.ui.chatlist.uimodel.ChatDateTime
import com.woowacourse.friendogly.presentation.ui.chatlist.uimodel.ChatDummy
import com.woowacourse.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import java.time.LocalDate
import java.time.LocalDateTime

fun ChatDummy.toUiModel(): ChatListUiModel =
    ChatListUiModel(
        title,
        body,
        numberOfPeople,
        unreadMessageCount,
        dateTime.classifyChatDateTime(),
        imageUrl,
    )

private fun LocalDateTime.classifyChatDateTime(): ChatDateTime {
    val today = LocalDate.now()
    val date = this.toLocalDate()

    return when {
        date.isEqual(today) -> ChatDateTime.Today(this)
        date.isEqual(today.minusDays(ChatListUiModel.YESTERDAY)) -> ChatDateTime.Yesterday(this)
        else -> ChatDateTime.NotRecent(this)
    }
}
