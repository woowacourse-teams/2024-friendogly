package com.happy.friendogly.chat.service;

import static com.happy.friendogly.pet.domain.Gender.MALE;
import static com.happy.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.support.ServiceTest;
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
    private Club club;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("n", "t", "https://a.com"));
        member2 = memberRepository.save(new Member("a", "b", "https://b.com"));
        pet = petRepository.save(
                new Pet(member1, "a", "d", LocalDate.now().minusYears(1), SMALL, MALE, "https://a.com"));
        club = clubRepository.save(
                Club.create("t", "c", "서울특별시", "성동구", "옥수동", 5, member1, Set.of(MALE), Set.of(SMALL), "https://a.com",
                        List.of(pet)));
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

    @DisplayName("1대1 채팅방 저장 과정에서, 이미 채팅방이 존재하면 기존 채팅방 ID를 반환한다.")
    @Transactional
    @Test
    void save_AlreadyExists() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createPrivate(member1, member2));

        // when
        SaveChatRoomRequest request = new SaveChatRoomRequest(member1.getId());
        SaveChatRoomResponse response = chatRoomCommandService.savePrivate(member2.getId(), request);

        // then
        assertThat(response.chatRoomId()).isEqualTo(chatRoom.getId());
    }
}
