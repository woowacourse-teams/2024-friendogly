package com.happy.friendogly.presentation.ui.chatlist.uimodel

import java.time.LocalDateTime

sealed interface ChatDateTime {
    val value: LocalDateTime

    data class Today(override val value: LocalDateTime) : ChatDateTime

    data class Yesterday(override val value: LocalDateTime) : ChatDateTime

    data class NotRecent(override val value: LocalDateTime) : ChatDateTime
}
