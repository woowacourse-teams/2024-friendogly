package com.happy.friendogly.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.happy.friendogly.local.dao.RecentPetsDao
import com.happy.friendogly.local.model.RecentPetEntity

@Database(entities = [RecentPetEntity::class], version = 1)
abstract class RecentPetsDatabase : RoomDatabase() {
    abstract fun recentPetDao(): RecentPetsDao
}
