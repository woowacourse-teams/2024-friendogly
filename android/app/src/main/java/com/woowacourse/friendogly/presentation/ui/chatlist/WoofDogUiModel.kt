package com.woowacourse.friendogly.presentation.ui.chatlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


@Parcelize
data class WoofDogUiModel(
    val imageUrl: String,
    val name: String,
    val size: String,
    val gender:String,
    val age: Int,
    val description: String,
): Parcelable
