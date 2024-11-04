package com.happy.friendogly.chatroom.service;

import static com.happy.friendogly.pet.domain.Gender.FEMALE;
import static com.happy.friendogly.pet.domain.Gender.FEMALE_NEUTERED;
import static com.happy.friendogly.pet.domain.Gender.MALE;
import static com.happy.friendogly.pet.domain.Gender.MALE_NEUTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chatroom.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chatroom.service.ChatRoomCommandService;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.service.ClubCommandService;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
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
    private ClubCommandService clubCommandService;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("n", "t", "https://a.com"));
        member2 = memberRepository.save(new Member("a", "b", "https://b.com"));
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

    @DisplayName("채팅방에서 나간다.")
    @Transactional
    @Test
    void leave() {
        // given
        Pet pet = petRepository.save(
                new Pet(member1, "asda", "asfdfdsa", LocalDate.now().minusYears(1), SizeType.SMALL, Gender.MALE,
                        "https://image.com"));

        Club club = clubRepository.save(Club.create(
                "모임", "모임입니다.", "서울특별시", "구구구", "동동동",
                5, member1, Set.of(Gender.MALE), Set.of(SizeType.SMALL), "https://image.com", List.of(pet)
        ));

        Pet pet2 = petRepository.save(
                new Pet(member2, "asda", "asfdfdsa", LocalDate.now().minusYears(1), SizeType.SMALL, Gender.MALE,
                        "https://image.com"));

        clubCommandService.joinClub(club.getId(), member2.getId(), new SaveClubMemberRequest(List.of(pet2.getId())));

        ChatRoom chatRoom = club.getChatRoom();

        // when
        chatRoomCommandService.leave(member2.getId(), chatRoom.getId());

        // then
        ChatRoom foundChatRoom = chatRoomRepository.getById(chatRoom.getId());
        assertAll(
                () -> assertThat(foundChatRoom.containsMember(member1)).isTrue(),
                () -> assertThat(foundChatRoom.containsMember(member2)).isFalse()
        );
    }

    @DisplayName("채팅방을 나가고 모임을 나가도 예외가 발생하지 않는다.")
    @Transactional
    @Test
    void leaveChatRoom_ThenLeaveClub() {
        // given
        Pet pet = petRepository.save(
                new Pet(member1, "n", "d", LocalDate.of(2020, 1, 1), SizeType.SMALL, MALE, "https://image.com"));

        Club club = clubRepository.save(Club.create(
                "선릉 강아지 모임",
                "강아지 모입입니다.",
                "서울특별시", "강남구", "대치동",
                5,
                member1,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SizeType.SMALL, SizeType.MEDIUM, SizeType.LARGE),
                "https://image.com/image.jpg",
                List.of(pet)
        ));

        ChatRoom chatRoom = club.getChatRoom();

        // when
        chatRoomCommandService.leave(member1.getId(), chatRoom.getId());

        // then
        assertDoesNotThrow(() -> clubCommandService.deleteClubMember(club.getId(), member1.getId()));
    }
}
