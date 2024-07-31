package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.member.domain.Member;

public record FindMyPrivateChatRoomResponse(
        Long privateChatRoomId,
        Member member,
        Member otherMember
) {

    public FindMyPrivateChatRoomResponse(PrivateChatRoom chatRoom) {
        this(chatRoom.getId(), chatRoom.getMember(), chatRoom.getOtherMember());
    }
}
