package com.woowacourse.friendogly.presentation.ui.chatlist.uimodel

import java.time.LocalDateTime

data class ChatListUiModel(
    val title: String,
    val body: String,
    val numberOfPeople: Int,
    val unreadMessageCount: Int,
    val dateTime: ChatDateTime,
    val imageUrl: String,
) {
    companion object {
        const val YESTERDAY = 1L
    }
}

// 더미데이터
data class ChatDummy(
    val title: String,
    val body: String,
    val numberOfPeople: Int,
    val unreadMessageCount: Int,
    val dateTime: LocalDateTime,
    val imageUrl: String,
)
