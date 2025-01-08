package com.happy.friendogly.presentation.ui.club.add.model

class ClubCounter(val count: Int = MAX_COUNT) {
    fun isValid(): Boolean {
        return count in MIN_COUNT..MAX_COUNT
    }

    companion object {
        const val MAX_COUNT = 10
        const val MIN_COUNT = 1
    }
}
