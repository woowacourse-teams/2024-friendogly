package com.woowacourse.friendogly.presentation.ui.group.list

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

private const val SEPARATOR_NUMBER_OF_PEOPLE = "/"
private const val SEPARATOR_GROUP_INFO = " · "
private const val NUMBER_MESSAGE = "명 참여"
private const val PARTICIPABLE_NAME = "모집중"
private const val UNPARTICIPABLE_NAME = "모집완료"

@SuppressLint("SetTextI18n")
@BindingAdapter("applyCurrentNumberOfPeople","applyMaximumNumberOfPeople")
fun TextView.bindNumberOfPeople(
    maximumNumberOfPeople: Int,
    currentNumberOfPeople: Int,
) {
    this.text =
        "${currentNumberOfPeople}${SEPARATOR_NUMBER_OF_PEOPLE}${maximumNumberOfPeople}${NUMBER_MESSAGE}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("applyGroupLocation","applyGroupLeader")
fun TextView.bindGroupInfo(
    groupLocation: String,
    groupLeader: String,
) {
    this.text = "${groupLeader}${SEPARATOR_GROUP_INFO}${groupLocation}"
}

@BindingAdapter("applyParticipable")
fun TextView.bindParticipableType(
    isParticipable: Boolean,
) {
    this.text = if (isParticipable) {
        PARTICIPABLE_NAME
    } else {
        UNPARTICIPABLE_NAME
    }
}
