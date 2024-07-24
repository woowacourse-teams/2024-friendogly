package com.woowacourse.friendogly.presentation.dialog

data class AlertDialogModel(
    val title: String?,
    val description: String?,
    val negativeContents: String,
    val positiveContents: String,
)