package com.happy.friendogly.chatroom.dto.request;

public record InviteToChatRoomRequest(
        Long receiverMemberId,
        Long chatRoomId
) {

}
