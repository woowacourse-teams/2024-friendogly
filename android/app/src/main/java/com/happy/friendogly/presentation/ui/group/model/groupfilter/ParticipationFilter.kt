package com.happy.friendogly.presentation.ui.group.model.groupfilter

enum class ParticipationFilter(val filterName: String) {
    POSSIBLE("참여 가능"),
    RECRUITMENT("모집중"),
    ENTIRE("전체");

    companion object {
        fun findParticipationFilter(filterName:String): ParticipationFilter?{
            return ParticipationFilter.entries.find {
                it.filterName == filterName
            }
        }
    }
}
