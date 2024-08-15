package com.happy.friendogly.presentation.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile

interface MainActivityActionHandler {
    fun navigateToClubDetailActivity(
        clubId: Long,
        resultLauncher: ActivityResultLauncher<Intent>,
    )

    fun navigateToClubAddActivity(resultLauncher: ActivityResultLauncher<Intent>)

    fun navigateToRegisterPet(petProfile: PetProfile?)

    fun navigateToProfileSetting(profile: Profile?)

    fun navigateToPetDetail(
        currentPage: Int,
        petsDetail: PetsDetail,
    )

    fun navigateToSetting()

    fun navigateToSettingLocation(resultLauncher: ActivityResultLauncher<Intent>)

    fun navigateToMyClub(isMyHead: Boolean)
}
