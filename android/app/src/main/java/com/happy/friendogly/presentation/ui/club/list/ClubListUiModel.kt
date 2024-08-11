package com.happy.friendogly.presentation.ui.club.list

import com.happy.friendogly.presentation.ui.club.common.model.ClubPet
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import kotlinx.datetime.LocalDateTime

data class ClubListUiModel(
    val clubId: Long,
    val filters: List<ClubFilter>,
    val clubPoster: String?,
    val canParticipate: Boolean,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val clubLocation: String,
    val clubLeaderName: String,
    val clubDate: LocalDateTime,
    val clubPets: List<ClubPet>,
)
