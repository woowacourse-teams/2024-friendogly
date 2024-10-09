package com.happy.friendogly.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.happy.friendogly.local.room.MessageTypeEntity
import com.happy.friendogly.remote.util.JavaLocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity(
    tableName = "chat_message",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoomEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("chat_room_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
@Serializable
data class ChatMessageEntity(
    @ColumnInfo(name = "created_at")
    @Serializable(with = JavaLocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Embedded(prefix = "member_")
    val member: ChatMemberEntity,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "type")
    val type: MessageTypeEntity,
    @ColumnInfo(name = "chat_room_id")
    val chatRoomId: Long = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    companion object {
        const val NOT_CONTENT = ""
    }
}
