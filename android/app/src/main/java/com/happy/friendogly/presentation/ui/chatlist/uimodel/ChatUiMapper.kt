package com.happy.friendogly.presentation.ui.chatlist.uimodel

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatUiModel

fun ChatComponent.Date.toUiModel() = ChatUiModel.Date(
    date = created
)

fun ChatComponent.Leave.toUiModel() = ChatUiModel.ComeOut(
    nickName = name,
    isCome = false
)

fun ChatComponent.Enter.toUiModel() = ChatUiModel.ComeOut(
    nickName = name,
    isCome = true
)

fun Message.Mine.toUiModel() = ChatUiModel.Mine(
    message = content,
    time = dateTime.toLocalTime()
)

fun Message.Other.toUiModel() = ChatUiModel.Other(
    nickName = name,
    message = content,
    time = dateTime.toLocalTime(),
    profileUrl = ""
)

