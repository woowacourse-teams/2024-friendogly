package com.happy.friendogly.presentation.ui.chatlist.uimodel

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatUiModel

fun ChatComponent.Date.toUiModel() =
    ChatUiModel.Date(
        date = createdAt.toLocalDate(),
    )

fun ChatComponent.Leave.toUiModel() =
    ChatUiModel.ComeOut(
        nickName = member.name,
        isCome = false,
    )

fun ChatComponent.Enter.toUiModel() =
    ChatUiModel.ComeOut(
        nickName = member.name,
        isCome = true,
    )

fun Message.Mine.toUiModel() =
    ChatUiModel.Mine(
        message = content,
        time = createdAt.toLocalTime(),
    )

fun Message.Other.toUiModel() =
    ChatUiModel.Other(
        nickName = member.name,
        message = content,
        time = createdAt.toLocalTime(),
        profileUrl = member.profileImageUrl,
        memberId = member.id,
    )
