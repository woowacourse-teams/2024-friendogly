package com.happy.friendogly.domain.model

import java.time.LocalDateTime

sealed interface Message : ChatComponent {
    val content: String

    data class Mine(
        override val content: String,
        val member: ChatMember,
        override val createdAt: LocalDateTime,
    ) : Message

    data class Other(
        val member: ChatMember,
        override val content: String,
        override val createdAt: LocalDateTime,
    ) : Message
}
