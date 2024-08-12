package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import com.happy.friendogly.domain.model.ChatMember

fun ChatMember.toUiModel(myMemberId: Long): JoinPeople =
    JoinPeople(
        nickName = this.memberName,
        isMe = myMemberId == memberId,
        isLeader = isOwner,
        profileUrl = memberProfileImageUrl,
        memberId = memberId,
    )
