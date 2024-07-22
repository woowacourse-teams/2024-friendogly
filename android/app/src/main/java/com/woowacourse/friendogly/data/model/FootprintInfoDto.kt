package com.woowacourse.friendogly.data.model

data class FootprintInfoDto(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    val petBirthDate: String,
    val petSizeType: String,
    val petGender: String,
    val footprintImageUrl: String,
    val isMine: Boolean,
)
