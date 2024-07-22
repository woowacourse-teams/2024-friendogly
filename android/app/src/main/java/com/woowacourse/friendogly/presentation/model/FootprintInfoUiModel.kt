package com.woowacourse.friendogly.presentation.model

data class FootprintInfoUiModel(
    val petName: String,
    val petDescription: String,
    val petAge: String,
    val petSizeType: String,
    val petGender: String,
    val footprintImageUrl: String,
    val isMine: Boolean,
)
