package com.happy.friendogly.presentation.ui.playground.uimodel

import com.happy.friendogly.presentation.ui.playground.model.PetDetail

data class PetDetailInfoUiModel(
    val memberId: Long,
    val memberName: String,
    val petDetail: PetDetail,
)
