package com.woowacourse.friendogly.presentation.ui

interface MainActivityActionHandler {
    fun navigateToGroupDetailActivity(groupId: Long)

    fun navigateToGroupAddActivity()

    fun navigateToRegisterDog()

    fun navigateToDogDetail()
}
