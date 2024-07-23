package com.woowacourse.friendogly.presentation.ui.group.list

interface GroupListActionHandler {
    fun loadGroup(groupId: Long)
    fun addGroup()
}
