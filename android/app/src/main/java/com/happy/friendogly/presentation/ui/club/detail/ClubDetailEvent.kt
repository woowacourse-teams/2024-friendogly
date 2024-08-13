package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

sealed interface ClubDetailEvent {
    data class OpenDogSelector(val filters: List<ClubFilter>) : ClubDetailEvent

    data class OpenDetailMenu(val clubDetailViewType: ClubDetailViewType) : ClubDetailEvent

    sealed interface Navigation : ClubDetailEvent {
        data object NavigateToChat : Navigation

        data object NavigateToHome : Navigation
    }

    data object FailLoadDetail : ClubDetailEvent

    data object FailParticipation : ClubDetailEvent
}
