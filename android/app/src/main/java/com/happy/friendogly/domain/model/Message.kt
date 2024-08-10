package com.happy.friendogly.domain.model

import java.time.LocalDateTime

sealed interface Message : ChatComponent {
    val content: String

    data class Mine(
        override val content: String,
        val dateTime: LocalDateTime,
    ) : Message

    data class Other(
        val memberId: Long,
        val name: String,
        override val content: String,
        val dateTime: LocalDateTime,
        val profileUrl: String?,
    ) : Message
}
