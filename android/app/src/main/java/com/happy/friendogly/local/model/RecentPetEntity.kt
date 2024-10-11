package com.happy.friendogly.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happy.friendogly.remote.util.JavaLocalDateTimeSerializer
import com.happy.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity(tableName = "recent_pet")
@Serializable
data class RecentPetEntity(
    @ColumnInfo(name = "memberId")
    val memberId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,
    @Serializable(with = LocalDateSerializer::class)
    @ColumnInfo(name = "birthday")
    val birthday: LocalDate,
    @ColumnInfo(name = "gender")
    val gender: GenderEntity,
    @ColumnInfo(name = "sizeType")
    val sizeType: SizeTypeEntity,
    @ColumnInfo(name = "created_at")
    @Serializable(with = JavaLocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
