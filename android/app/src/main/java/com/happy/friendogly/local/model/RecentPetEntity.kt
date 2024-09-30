package com.happy.friendogly.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recent_pet")
@Serializable
data class RecentPetEntity(
    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "memberId")
    val memberId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
