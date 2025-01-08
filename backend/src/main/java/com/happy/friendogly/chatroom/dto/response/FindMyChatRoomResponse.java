package com.happy.friendogly.chatroom.dto.response;

import java.util.List;

public record FindMyChatRoomResponse(
        Long myMemberId,
        List<ChatRoomDetail> chatRooms
) {

}
