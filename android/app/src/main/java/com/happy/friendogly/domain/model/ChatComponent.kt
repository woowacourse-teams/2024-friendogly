package com.happy.friendogly.domain.model

import java.time.LocalDate
import java.time.LocalDateTime


sealed interface ChatComponent {
    data class Date(val created: LocalDate) : ChatComponent

    data class Enter(val name: String) : ChatComponent

    data class Leave(val name: String) : ChatComponent
}


