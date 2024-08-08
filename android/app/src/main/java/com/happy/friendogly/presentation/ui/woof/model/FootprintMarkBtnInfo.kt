package com.happy.friendogly.presentation.ui.woof.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration

data class FootprintMarkBtnInfo(val createdAt: LocalDateTime?, val hasPet: Boolean) {
    fun isMarkBtnClickable(): Boolean {
        if (createdAt == null) return true
        val duration = Duration.between(createdAt.toJavaLocalDateTime(), java.time.LocalDateTime.now())
        return duration.toMillis() > SECONDS_LIMIT * MILLI_SECONDS
    }

    fun remainingTime(): Int {
        if (createdAt == null) return NO_REMAINING_TIME
        val duration = Duration.between(createdAt.toJavaLocalDateTime(), java.time.LocalDateTime.now())
        val remainingTimeMillis = (SECONDS_LIMIT * MILLI_SECONDS) - duration.toMillis()
        if (remainingTimeMillis <= 0) {
            return NO_REMAINING_TIME
        }
        return (remainingTimeMillis / MILLI_SECONDS).toInt()
    }

    companion object {
        private const val SECONDS_LIMIT = 30
        private const val MILLI_SECONDS = 1000
        private const val NO_REMAINING_TIME = 0
    }
}
