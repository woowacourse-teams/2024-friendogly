package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel

interface ClubDetailNavigation {
    fun navigateToModify()

    fun navigateToPrevWithReload()

    fun navigateToProfile(clubDetailProfileUiModel: ClubDetailProfileUiModel)
}
