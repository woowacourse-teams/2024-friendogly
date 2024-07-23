package com.woowacourse.friendogly.presentation.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseToLocalDate(
    date: String?,
    pattern: String = "yyyy-MM-dd",
): LocalDate {
    val dateFormatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(date, dateFormatter)
}

fun parseToLocalDateTimeOrNull(
    dateTime: String?,
    pattern: String = "yyyy-MM-dd HH:mm:ss.SSS",
): LocalDateTime? {
    if (dateTime == null) {
        return null
    }
    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(dateTime, dateTimeFormatter)
}

fun parseToLocalDateTime(
    dateTime: String,
    pattern: String = "yyyy-MM-dd HH:mm:ss.SSS",
): LocalDateTime {
    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(dateTime, dateTimeFormatter)
}
