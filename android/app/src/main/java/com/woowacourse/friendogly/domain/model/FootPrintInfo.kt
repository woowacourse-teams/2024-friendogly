package com.woowacourse.friendogly.domain.model

import java.time.LocalDate

data class FootPrintInfo(
    val imageUrl: String,
    val name: String,
    val size: String,
    val gender: String,
    val isNeutered: Boolean,
    val birthDate: LocalDate,
    val description: String,
)
