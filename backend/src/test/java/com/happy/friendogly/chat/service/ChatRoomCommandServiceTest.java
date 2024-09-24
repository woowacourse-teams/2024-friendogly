package com.happy.friendogly.chat.service;

import static com.happy.friendogly.pet.domain.Gender.FEMALE;
import static com.happy.friendogly.pet.domain.Gender.FEMALE_NEUTERED;
import static com.happy.friendogly.pet.domain.Gender.MALE;
import static com.happy.friendogly.pet.domain.Gender.MALE_NEUTERED;
import static com.happy.friendogly.pet.domain.SizeType.LARGE;
import static com.happy.friendogly.pet.domain.SizeType.MEDIUM;
import static com.happy.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatRoomCommandServiceTest {

    @Autowired
    private ChatRoomCommandService chatRoomCommandService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("n", "t", "https://a.com"));
        member2 = memberRepository.save(new Member("a", "b", "https://b.com"));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("""
                SET REFERENTIAL_INTEGRITY FALSE;
                                
                DELETE FROM chat_room;
                DELETE FROM chat_room_member;
                DELETE FROM club;
                DELETE FROM club_member;
                DELETE FROM club_pet;
                DELETE FROM member;
                DELETE FROM pet;
                                
                SET REFERENTIAL_INTEGRITY TRUE;
                """);
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

    @DisplayName("채팅방에 참여한다.")
    @Transactional
    @Test
    void enter() {
        // given
        Pet pet = petRepository.save(
                new Pet(member1, "강아지", "귀여워요", LocalDate.of(2020, 1, 1), SMALL, MALE, "https://image.com"));

        Club club = clubRepository.save(Club.create(
                "서울 강아지 모임",
                "매주 월요일에 모여요",
                "서울특별시",
                "서초구",
                "서초동",
                5,
                member1,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE),
                "https://image.com",
                List.of(pet)
        ));

        // when
        chatRoomCommandService.enter(member1.getId(), club.getChatRoom().getId());

        // then
        ChatRoom foundChatRoom = chatRoomRepository.getById(club.getChatRoom().getId());
        assertThat(foundChatRoom.countMembers()).isOne();
    }

    @DisplayName("채팅방에서 나간다.")
    @Transactional
    @Test
    void leave() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createGroup(5));
        Member member3 = memberRepository.save(new Member("john", "aaa111ab", "https://image.com"));

        chatRoom.addMember(member1);
        chatRoom.addMember(member2);
        chatRoom.addMember(member3);

        // when
        chatRoomCommandService.leave(member2.getId(), chatRoom.getId());

        // then
        ChatRoom foundChatRoom = chatRoomRepository.getById(chatRoom.getId());
        assertAll(
                () -> assertThat(foundChatRoom.containsMember(member1)).isTrue(),
                () -> assertThat(foundChatRoom.containsMember(member2)).isFalse(),
                () -> assertThat(foundChatRoom.containsMember(member3)).isTrue()
        );
    }

    @DisplayName("모임에 참여하지 않은 경우 채팅방에 참여할 수 없다.")
    @Test
    void enter_Fail_NotInClub() {
        // given
        Pet pet = petRepository.save(
                new Pet(member1, "강아지", "귀여워요", LocalDate.of(2020, 1, 1), SMALL, MALE, "https://image.com"));

        Club club = clubRepository.save(Club.create(
                "서울 강아지 모임",
                "매주 월요일에 모여요",
                "서울특별시",
                "서초구",
                "서초동",
                5,
                member1,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE),
                "https://image.com",
                List.of(pet)
        ));

        // when & then
        assertThatThrownBy(() -> chatRoomCommandService.enter(member2.getId(), club.getChatRoom().getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 참여해야만 채팅에 참여할 수 있습니다.");
    }
}
