package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

// TODO: 뷰 플로우에 따라서 활용할 지 결정될 것 같음
@Serializable
data class ClubSaveResponse(
    val id: Long,
    val title: String,
    val content: String,
    val ownerMemberName: String,
    val address: String,
    val status: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val allowedGender: List<GenderResponse>,
    val allowedSize: List<SizeTypeResponse>,
    val memberCapacity: Int,
    val currentMemberCount: Int,
    val imageUrl: String,
    val petImageUrls: List<String>,
    val isMine: Boolean,
)
