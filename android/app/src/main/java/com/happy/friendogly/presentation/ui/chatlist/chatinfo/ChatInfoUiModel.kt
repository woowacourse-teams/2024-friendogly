package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType

data class ChatInfoUiModel(
    val clubId:Long,
    val dogSize: List<SizeType>,
    val dogGender: List<Gender>,
)

data class JoinPeople(
    val nickName: String,
    val isMe: Boolean,
    val isLeader: Boolean,
    val profileUrl: String,
    val memberId: Long,
)
