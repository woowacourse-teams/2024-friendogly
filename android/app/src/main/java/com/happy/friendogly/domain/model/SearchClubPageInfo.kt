package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDateTime

data class SearchClubPageInfo(
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val lastFoundCreatedAt: LocalDateTime = LocalDateTime.parse(DEFAULT_FOUND_CREATE),
    val lastFoundId: Long = DEFAULT_FOUND_ID,
) {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 5
        private const val DEFAULT_FOUND_CREATE = "9999-12-31T23:59:59"
        private const val DEFAULT_FOUND_ID = 0x7fffffffffffffff
    }
}
