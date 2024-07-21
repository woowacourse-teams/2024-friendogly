package com.woowacourse.friendogly.presentation.ui.chatlist.chat

import java.time.LocalDate
import java.time.LocalTime

sealed interface ChatUiModel {
    data class Date(val date: LocalDate) : ChatUiModel
    data class ComeOut(val nickName: String, val isCome:Boolean) : ChatUiModel

    data class Mine(val message: String, val time: LocalTime) : ChatUiModel

    data class Other(
        val nickName: String,
        val profileUrl: String,
        val message: String,
        val time: LocalTime
    ) : ChatUiModel
}


