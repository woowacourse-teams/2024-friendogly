package com.woowacourse.friendogly.domain.model

import java.time.Duration
import java.time.LocalDateTime

data class FootprintMarkBtnInfo(val createdAt: LocalDateTime?) {
    fun isMarkBtnClickable(currentDateTime: LocalDateTime = LocalDateTime.now()): Boolean {
        if (createdAt == null) return true
        val duration = Duration.between(createdAt, currentDateTime)
        return duration.toMillis() > SECONDS_LIMIT * MILLI_SECONDS
    }

    fun remainingTime(currentDateTime: LocalDateTime = LocalDateTime.now()): String {
        if (createdAt == null) return NO_REMAINING_TIME
        val duration = Duration.between(createdAt, currentDateTime)
        val remainingTimeMillis = (SECONDS_LIMIT * MILLI_SECONDS) - duration.toMillis()
        if (remainingTimeMillis <= 0) {
            return NO_REMAINING_TIME
        }
        return (remainingTimeMillis / MILLI_SECONDS).toString()
    }

    companion object {
        private const val SECONDS_LIMIT = 30
        private const val MILLI_SECONDS = 1000
        private const val NO_REMAINING_TIME = "0"
    }
}
