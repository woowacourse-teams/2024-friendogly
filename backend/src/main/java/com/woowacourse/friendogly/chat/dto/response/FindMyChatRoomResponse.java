package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import java.util.List;

public record FindMyChatRoomResponse(
        Long chatRoomId,
        List<String> memberNames
) {

    public FindMyChatRoomResponse(ChatRoom chatRoom) {
        this(
                chatRoom.getId(),
                chatRoom.findMemberNames()
        );
    }
}
