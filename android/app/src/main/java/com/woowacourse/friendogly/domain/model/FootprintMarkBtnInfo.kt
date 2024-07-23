package com.woowacourse.friendogly.domain.model

import java.time.LocalDateTime

data class FootprintMarkBtnInfo(val createdAt: LocalDateTime?) {
    fun isMarkBtnClickable(currentDateTime: LocalDateTime = LocalDateTime.now()): Boolean {
        return true
//        val duration = Duration.between(createdAt, currentDateTime)
//        return duration.toMillis() <= SECONDS_LIMIT * MILLI_SECONDS
    }

    fun remainingTime(currentDateTime: LocalDateTime = LocalDateTime.now()): String {
        return "30"
//        val duration = Duration.between(createdAt, currentDateTime)
//        val remainingTime = SECONDS_LIMIT - duration.seconds.coerceIn(MIN_REMAINING_TIME, MAX_REMAINING_TIME)
//        return "$remainingTime"
    }

    companion object {
        private const val MIN_REMAINING_TIME = 1L
        private const val MAX_REMAINING_TIME = 30L
        private const val SECONDS_LIMIT = 30
        private const val MILLI_SECONDS = 1000
    }
}
