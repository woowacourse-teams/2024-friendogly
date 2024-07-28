package com.happy.friendogly.presentation.ui.group.model.groupfilter

sealed class ParticipationFilter(val filterName: String) {
    data object POSSIBLE : ParticipationFilter(POSSIBLE_FILTER_NAME)
    data object RECRUITMENT : ParticipationFilter(RECRUITMENT_FILTER_NAME)
    data object ENTIRE : ParticipationFilter(ENTIRE_FILTER_NAME)

    companion object {
        fun findParticipationFilter(filterName: String): ParticipationFilter? {
            return when (filterName) {
                POSSIBLE_FILTER_NAME -> POSSIBLE
                RECRUITMENT_FILTER_NAME -> RECRUITMENT
                ENTIRE_FILTER_NAME -> ENTIRE
                else -> null
            }
        }

        const val POSSIBLE_FILTER_NAME = "참여 가능"
        const val RECRUITMENT_FILTER_NAME = "모집중"
        const val ENTIRE_FILTER_NAME = "전체"
    }
}
