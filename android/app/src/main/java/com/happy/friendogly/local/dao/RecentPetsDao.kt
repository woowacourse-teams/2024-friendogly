package com.happy.friendogly.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.happy.friendogly.local.model.RecentPetEntity

@Dao
interface RecentPetsDao {
    @Query("SELECT * FROM recent_pet WHERE memberId = :id")
    suspend fun getRecentPetById(id: Long): RecentPetEntity?

    @Query("SELECT * FROM recent_pet")
    suspend fun getAllRecentPet(): List<RecentPetEntity>

    @Insert
    suspend fun insertRecentPet(recentPetEntity: RecentPetEntity)
}
