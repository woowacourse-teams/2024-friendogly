package com.happy.friendogly.presentation.ui.group.list

interface GroupListActionHandler {
    fun loadGroup(groupId: Long)

    fun addGroup()

    fun selectParticipationFilter()

    fun selectSizeFilter()

    fun selectGenderFilter()
}
