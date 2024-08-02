package com.happy.friendogly.presentation.ui

import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile

interface MainActivityActionHandler {
    fun navigateToGroupDetailActivity(groupId: Long)

    fun navigateToGroupAddActivity()

    fun navigateToRegisterDog()

    fun navigateToProfileSetting(profile: Profile?)

    fun navigateToPetDetail(
        currentPage: Int,
        petsDetail: PetsDetail,
    )

    fun navigateToSetting()
}
