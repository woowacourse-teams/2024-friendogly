package com.happy.friendogly.chatroom.dto.response;

import java.util.List;

public record FindMyChatRoomResponseV2(
        Long myMemberId,
        List<ChatRoomDetailV2> chatRooms
) {

}
