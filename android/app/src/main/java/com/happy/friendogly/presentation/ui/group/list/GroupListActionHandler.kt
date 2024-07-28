package com.happy.friendogly.presentation.ui.group.list

import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

interface GroupListActionHandler {
    fun loadGroup(groupId: Long)

    fun addGroup()

    fun selectParticipationFilter()

    fun selectSizeFilter()

    fun selectGenderFilter()

    fun removeFilter(groupFilter: GroupFilter)
}
