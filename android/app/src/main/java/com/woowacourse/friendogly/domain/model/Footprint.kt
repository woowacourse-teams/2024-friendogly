package com.woowacourse.friendogly.domain.model

import java.time.LocalDateTime

data class Footprint(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val isMine: Boolean,
    val imageUrl: String?,
) {
    fun isVisible(currentDateTime: LocalDateTime = LocalDateTime.now()): Boolean {
        return true
//        val duration = Duration.between(currentDateTime, createdAt)
//        return duration.toHours() <= HOURS_LIMIT
    }

    companion object {
        private const val HOURS_LIMIT = 24
    }
}
