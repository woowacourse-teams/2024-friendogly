package com.woowacourse.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PrivateChatRoomCommandServiceTest extends ChatRoomServiceTest {

    @Autowired
    private PrivateChatRoomCommandService privateChatRoomCommandService;

    @DisplayName("새로운 1대1 채팅방을 만들 수 있다.")
    @Test
    void save() {
        // when
        InvitePrivateChatRoomResponse response = privateChatRoomCommandService
                .save(member1.getId(), member2.getId());

        // then
        assertThat(response.privateChatRoomId()).isNotNull();
    }

    @DisplayName("1대1 채팅방을 만들 때, 기존에 방이 있는 경우 기존의 방 ID를 반환한다.")
    @Test
    void save_AlreadyExists() {
        // given
        Long savedChatRoomId = privateChatRoomCommandService
                .save(member2.getId(), member1.getId())
                .privateChatRoomId();

        // when
        InvitePrivateChatRoomResponse response = privateChatRoomCommandService
                .save(member1.getId(), member2.getId());

        // then
        assertThat(response.privateChatRoomId()).isEqualTo(savedChatRoomId);
    }

    @DisplayName("1대1 채팅방을 만들 때, 자기 자신을 초대하면 예외가 발생한다.")
    @Test
    void save_InviteMyself() {
        // when - then
        assertThatThrownBy(
                () -> privateChatRoomCommandService
                        .save(member1.getId(), member1.getId())
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("자기 자신을 채팅방에 초대할 수 없습니다.");
    }
}
