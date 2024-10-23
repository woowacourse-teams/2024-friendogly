package com.happy.friendogly.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.happy.friendogly.local.model.RecentPetEntity
import java.time.LocalDateTime

@Dao
interface RecentPetsDao {
    @Query("SELECT * FROM recent_pet WHERE memberId = :id")
    suspend fun getRecentPetById(id: Long): RecentPetEntity?

    @Query("SELECT * FROM recent_pet ORDER BY created_at DESC")
    suspend fun getAllRecentPet(): List<RecentPetEntity>

    @Query("DELETE FROM recent_pet WHERE petId = :petId AND DATE(created_at) = DATE(:createdAt)")
    suspend fun deleteByPetIdAndCreatedAt(
        petId: Long,
        createdAt: LocalDateTime,
    )

    @Insert
    suspend fun insertRecentPet(recentPetEntity: RecentPetEntity)

    @Transaction
    suspend fun insertOrUpdateRecentPet(recentPetEntity: RecentPetEntity) {
        deleteByPetIdAndCreatedAt(recentPetEntity.petId, recentPetEntity.createdAt)
        insertRecentPet(recentPetEntity)
    }
}
