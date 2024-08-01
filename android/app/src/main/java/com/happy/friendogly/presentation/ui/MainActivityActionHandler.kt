package com.happy.friendogly.presentation.ui

interface MainActivityActionHandler {
    fun navigateToGroupDetailActivity(groupId: Long)

    fun navigateToGroupAddActivity()

    fun navigateToRegisterDog()

    fun navigateToPetDetail()

    fun navigateToSetting()
}
