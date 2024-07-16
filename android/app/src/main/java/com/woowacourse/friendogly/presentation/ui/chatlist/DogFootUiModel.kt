package com.woowacourse.friendogly.presentation.ui.chatlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogFootUiModel(
    val memberId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
): Parcelable
