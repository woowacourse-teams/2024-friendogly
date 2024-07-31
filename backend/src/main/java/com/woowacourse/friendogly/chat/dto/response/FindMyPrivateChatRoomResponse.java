package com.woowacourse.friendogly.chat.dto.response;

public record FindMyPrivateChatRoomResponse(
        Long privateChatRoomId,
        String oppositeMemberName
) {

}
