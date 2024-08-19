package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyUiModel
import kotlinx.datetime.LocalDateTime

data class ClubDetailUiModel(
    val clubId: Long,
    val filters: List<ClubFilter>,
    val clubPoster: String? = null,
    val clubDetailViewType: ClubDetailViewType,
    val isUserPetEmpty: Boolean,
    val title: String,
    val content: String,
    val maximumNumberOfPeople: Int,
    val currentNumberOfPeople: Int,
    val clubLocation: String,
    val clubLeaderName: String,
    val clubLeaderImage: String? = null,
    val clubDate: LocalDateTime,
    val userProfiles: List<ClubDetailProfileUiModel>,
    val petProfiles: List<ClubDetailProfileUiModel>,
) {
    fun toClubModifyUiModel(): ClubModifyUiModel {
        return ClubModifyUiModel(
            title = title,
            content = content,
            clubPoster = clubPoster,
        )
    }
}
