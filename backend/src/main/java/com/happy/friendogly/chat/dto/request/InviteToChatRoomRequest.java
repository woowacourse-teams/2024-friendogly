package com.happy.friendogly.chat.dto.request;

public record InviteToChatRoomRequest(
        Long receiverMemberId,
        Long chatRoomId
) {

}
