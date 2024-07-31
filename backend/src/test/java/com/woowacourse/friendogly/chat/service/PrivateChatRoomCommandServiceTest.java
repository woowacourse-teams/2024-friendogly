package com.woowacourse.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        assertAll(
                () -> assertThat(response.privateChatRoomId()).isNotNull(),
                () -> assertThat(privateChatRoomRepository.count()).isEqualTo(1)
        );
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
        assertAll(
                () -> assertThat(response.privateChatRoomId()).isEqualTo(savedChatRoomId),
                () -> assertThat(privateChatRoomRepository.count()).isEqualTo(1)
        );
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

    @DisplayName("1:1 채팅방을 나갈 수 있다.")
    @Transactional
    @Test
    void leave() {
        // given
        PrivateChatRoom chatRoom = privateChatRoomRepository.save(new PrivateChatRoom(member1, member2));

        // when
        privateChatRoomCommandService.leave(member1.getId(), chatRoom.getId());

        // then
        PrivateChatRoom savedChatRoom = privateChatRoomRepository.findById(chatRoom.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(savedChatRoom.getMember()).isNull();
    }

    @DisplayName("자신이 참여하지 않은 1:1 채팅방은 나갈 수 없다.")
    @Test
    void leave_NotMyChatRoom() {
        // given
        PrivateChatRoom chatRoom = privateChatRoomRepository.save(new PrivateChatRoom(member1, member2));
        Member member3 = memberRepository.save(
                new Member("땡이", "aa99", "a@test.com", "https://picsum.photos/200"));

        // when - then
        assertThatThrownBy(() -> privateChatRoomCommandService.leave(member3.getId(), chatRoom.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("자신이 참여한 채팅방만 나갈 수 있습니다.");
    }

    @DisplayName("2명 모두 1:1 채팅방을 나가면 채팅방이 삭제된다.")
    @Transactional
    @Test
    void leave_AllMembers() {
        // given
        PrivateChatRoom chatRoom = privateChatRoomRepository.save(new PrivateChatRoom(member1, member2));

        // when
        privateChatRoomCommandService.leave(member1.getId(), chatRoom.getId());
        privateChatRoomCommandService.leave(member2.getId(), chatRoom.getId());

        // then
        assertThat(privateChatRoomRepository.findById(chatRoom.getId())).isNotPresent();
    }
}
