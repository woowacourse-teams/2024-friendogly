package com.happy.friendogly.local.mapper

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}
