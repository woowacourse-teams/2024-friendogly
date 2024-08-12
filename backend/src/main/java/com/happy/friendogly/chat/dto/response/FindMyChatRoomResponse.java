package com.happy.friendogly.chat.dto.response;

import java.util.List;

public record FindMyChatRoomResponse(
        Long myMemberId,
        List<ChatRoomDetail> chatRooms
) {

}
