package com.happy.friendogly.presentation.ui.chatlist

import com.happy.friendogly.domain.model.ChatRoom
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatDateTime
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatDummy
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import java.time.LocalDate
import java.time.LocalDateTime

fun ChatRoom.toUiModel(): ChatListUiModel =
    ChatListUiModel(
        clubName,
        "",
        memberCount,
        0,
        LocalDateTime.now().classifyChatDateTime(),
        clubImageUrl,
        chatRoomId = chatRoomId
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
