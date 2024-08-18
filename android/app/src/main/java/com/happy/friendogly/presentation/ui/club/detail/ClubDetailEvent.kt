package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.domain.model.ClubParticipation
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

sealed interface ClubDetailEvent {
    data class OpenDogSelector(val filters: List<ClubFilter>) : ClubDetailEvent

    data class OpenDetailMenu(val clubDetailViewType: ClubDetailViewType) : ClubDetailEvent

    sealed interface Navigation : ClubDetailEvent {
        data class NavigateToChat(val clubParticipation: ClubParticipation?) : Navigation

        data object NavigateToHome : Navigation

        data object NavigateToRegisterPet: Navigation
    }

    data object FailLoadDetail : ClubDetailEvent

    data object FailParticipation : ClubDetailEvent
}
