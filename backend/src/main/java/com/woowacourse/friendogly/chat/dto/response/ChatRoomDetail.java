package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.club.domain.Club;

public record ChatRoomDetail(
        Long chatRoomId,
        int memberCount,
        String clubName,
        String clubImageUrl
) {

    public ChatRoomDetail(ChatRoom chatRoom, Club club) {
        this(
                chatRoom.getId(),
                chatRoom.countMembers(),
                club.getTitle().getValue(),
                club.getImageUrl()
        );
    }
}
