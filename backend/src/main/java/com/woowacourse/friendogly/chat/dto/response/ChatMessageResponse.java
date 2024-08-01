package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.MessageType;

public record ChatMessageResponse(
        MessageType messageType,
        String senderName,
        String content
) {

}
