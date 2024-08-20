package com.happy.friendogly.domain.model

import java.time.LocalDateTime

sealed interface ChatComponent {
    val createdAt: LocalDateTime

    data class Date(override val createdAt: LocalDateTime) : ChatComponent

    data class Enter(val member: ChatMember, override val createdAt: LocalDateTime) : ChatComponent

    data class Leave(val member: ChatMember, override val createdAt: LocalDateTime) : ChatComponent
}
