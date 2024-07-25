package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration

data class FootprintMarkBtnInfo(val createdAt: LocalDateTime?) {
    fun isMarkBtnClickable(currentDateTime: LocalDateTime = LocalDateTime.parse(java.time.LocalDateTime.now().toString())): Boolean {
        if (createdAt == null) return true
        val duration = Duration.between(createdAt.toJavaLocalDateTime(), currentDateTime.toJavaLocalDateTime())
        return duration.toMillis() > SECONDS_LIMIT * MILLI_SECONDS
    }

    fun remainingTime(currentDateTime: LocalDateTime = LocalDateTime.parse(java.time.LocalDateTime.now().toString())): Int {
        if (createdAt == null) return NO_REMAINING_TIME
        val duration = Duration.between(createdAt.toJavaLocalDateTime(), currentDateTime.toJavaLocalDateTime())
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
