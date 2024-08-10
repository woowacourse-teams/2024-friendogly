package com.happy.friendogly.presentation.ui

import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile

interface MainActivityActionHandler {
    fun navigateToClubDetailActivity(clubId: Long)

    fun navigateToClubAddActivity()

    fun navigateToRegisterPet(petProfile: PetProfile?)

    fun navigateToProfileSetting(profile: Profile?)

    fun navigateToPetDetail(
        currentPage: Int,
        petsDetail: PetsDetail,
    )

    fun navigateToSetting()

    fun navigateToSettingLocation()
}
