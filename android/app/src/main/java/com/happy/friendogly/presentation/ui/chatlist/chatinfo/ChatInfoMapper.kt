package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import com.happy.friendogly.domain.model.ChatMember

fun ChatMember.toUiModel(myMemberId: Long): JoinPeople =
    JoinPeople(
        nickName = this.name,
        isMe = myMemberId == id,
        isLeader = isOwner,
        profileUrl = profileImageUrl,
        memberId = id,
    )
