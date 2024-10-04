package com.happy.friendogly.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.happy.friendogly.local.dao.RecentPetsDao
import com.happy.friendogly.local.mapper.LocalDateConverter
import com.happy.friendogly.local.mapper.LocalDateTimeConverter
import com.happy.friendogly.local.model.RecentPetEntity

@Database(entities = [RecentPetEntity::class], version = 1)
@TypeConverters(value = [LocalDateTimeConverter::class, LocalDateConverter::class])
abstract class RecentPetsDatabase : RoomDatabase() {
    abstract fun recentPetDao(): RecentPetsDao
}
