package com.woowacourse.friendogly.presentation.ui.woof

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogUiModel(
    val imageUrl: String,
    val name: String,
    val size: String,
    val gender: String,
    val age: Int,
    val description: String,
) : Parcelable
