package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

sealed interface ClubDetailEvent {
    data class OpenDogSelector(val filters: List<ClubFilter>) : ClubDetailEvent

    data class OpenDetailMenu(val clubDetailViewType: ClubDetailViewType) : ClubDetailEvent

    data object SaveReLoadState : ClubDetailEvent

    sealed interface Navigation : ClubDetailEvent {
        data class NavigateToChat(val chatRoomId: Long) : Navigation

        data object NavigateToHome : Navigation

        data object NavigateToRegisterPet : Navigation

        data object NavigateSelectState : Navigation

        data class NavigateProfileDetail(val clubDetailProfileUiModel: ClubDetailProfileUiModel) : Navigation
    }
}
