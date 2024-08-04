package com.happy.friendogly.presentation.ui

import com.happy.friendogly.presentation.ui.petdetail.PetsDetail

interface MainActivityActionHandler {
    fun navigateToGroupDetailActivity(groupId: Long)

    fun navigateToGroupAddActivity()

    fun navigateToRegisterDog()

    fun navigateToPetDetail(
        currentPage: Int,
        petsDetail: PetsDetail,
    )

    fun navigateToSetting()

    fun navigateToSettingLocation()
}
