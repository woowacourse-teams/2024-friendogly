package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.presentation.ui.woof.model.PetDetail

data class PetDetailInfoUiModel(
    val memberId: Long,
    val memberName: String,
    val petDetail: PetDetail,
    val viewType: Int,
)
