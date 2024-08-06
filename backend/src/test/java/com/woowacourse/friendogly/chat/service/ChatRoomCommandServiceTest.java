package com.woowacourse.friendogly.chat.service;

import static com.woowacourse.friendogly.pet.domain.Gender.MALE;
import static com.woowacourse.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class ChatRoomCommandServiceTest extends ServiceTest {

    @Autowired
    private ChatRoomCommandService chatRoomCommandService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Member member1;
    private Member member2;
    private Pet pet;
    private ChatRoom chatRoom;
    private Club club;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("n", "t", "a@a.com", "https://a.com"));
        member2 = memberRepository.save(new Member("a", "b", "b@b.com", "https://b.com"));
        pet = petRepository.save(
                new Pet(member1, "a", "d", LocalDate.now().minusYears(1), SMALL, MALE, "https://a.com"));
        club = clubRepository.save(
                Club.create("t", "c", "서울특별시", "성동구", "옥수동", 5, member1, Set.of(MALE), Set.of(SMALL), "https://a.com",
                        List.of(pet)));
        chatRoom = club.getChatRoom();
    }

    @DisplayName("새로운 1대1 채팅방을 개설할 수 있다.")
    @Transactional
    @Test
    void save() {
        // given
        SaveChatRoomRequest request = new SaveChatRoomRequest(member2.getId());

        // when
        SaveChatRoomResponse response = chatRoomCommandService.savePrivate(member1.getId(), request);
        Long chatRoomId = response.chatRoomId();

        // then
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        assertThat(chatRoom.findMembers()).containsExactly(member1, member2);
    }

    @DisplayName("채팅방에 참여한 경우, 채팅방에서 나갈 수 있다.")
    @Transactional
    @Test
    void leave() {
        // given
        ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.createPrivate(member1, member2));

        // when
        chatRoomCommandService.leave(member1.getId(), newChatRoom.getId());

        // then
        assertThat(newChatRoom.countMembers()).isOne();
    }

    @DisplayName("모임에 참여하지 않은 경우, 모임 채팅방에서 나갈 수 없다.")
    @Test
    void leave_Fail_NotInClub() {
        // when - then
        assertThatThrownBy(() -> chatRoomCommandService.leave(member2.getId(), chatRoom.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("채팅에 참여한 상태가 아닙니다.");
    }
}
