package com.happy.friendogly.domain.model

import java.time.LocalDateTime

sealed interface Message : ChatComponent {
    val content: String

    class Mine(
         content: String,
        val member: ChatMember,
        override val createdAt: LocalDateTime,
    ) : Message {
        override val content: String = content.trim()
    }

    class Other(
        content: String,
        val member: ChatMember,
        override val createdAt: LocalDateTime,
    ) : Message{
        override val content: String = content.trim()
    }
}
