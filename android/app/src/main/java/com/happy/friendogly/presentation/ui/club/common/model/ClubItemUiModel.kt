package com.happy.friendogly.presentation.ui.club.common.model

import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import kotlinx.datetime.LocalDateTime

data class ClubItemUiModel(
    val clubId: Long,
    val clubPoster: String?,
    val clubState: ClubState,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val clubLocation: String,
    val clubLeaderName: String,
    val clubDate: LocalDateTime,
    val clubPets: List<ClubPet>,
)
