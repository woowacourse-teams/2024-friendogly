package com.happy.friendogly.chatroom.service;

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

import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.dto.response.ChatRoomDetail;
import com.happy.friendogly.chatroom.dto.response.ChatRoomDetailV2;
import com.happy.friendogly.chatroom.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chatroom.dto.response.FindClubDetailsResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponseV2;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class ChatRoomQueryServiceTest extends ServiceTest {

    @Autowired
    private ChatRoomQueryService chatRoomQueryService;

    private Member member1;
    private Member member2;
    private Member member3;

    private ChatRoom chatRoom1;
    private ChatRoom chatRoom2;

    private Pet pet1;
    private Pet pet2;
    private Pet pet3;

    private Club club1;
    private Club club2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("트레", "a4a82b2g", "https://img1.com/image.jpg"));
        member2 = memberRepository.save(new Member("벼리", "fb7123cc", "https://img2.com/image.jpg"));
        member3 = memberRepository.save(new Member("위브", "169a7fb9", "https://img3.com/image.jpg"));

        pet1 = petRepository.save(new Pet(member1, "땡이", "귀여워요", LocalDate.now().minusYears(1),
                SMALL, MALE, "https://image.com/image1.jpg"));
        pet2 = petRepository.save(new Pet(member2, "누룽지", "노래요", LocalDate.now().minusYears(1),
                MEDIUM, FEMALE, "https://image.com/image2.jpg"));
        pet3 = petRepository.save(new Pet(member3, "보리", "작아요", LocalDate.now().minusYears(1),
                SMALL, MALE, "https://image.com/image3.jpg"));

        club1 = Club.create(
                "모임 제목1",
                "신나는 모임입니다!",
                "서울특별시",
                "성동구",
                "금호동",
                5,
                member1,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE),
                "https://image.com",
                List.of(pet1)
        );
        clubRepository.save(club1);

        club2 = Club.create(
                "모임 제목2",
                "강아지 모임 해요",
                "서울특별시",
                "성동구",
                "금호동",
                5,
                member1,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE),
                "https://image.com",
                List.of(pet1)
        );
        clubRepository.save(club2);

        chatRoom1 = club1.getChatRoom();
        chatRoom2 = club2.getChatRoom();
    }

    @DisplayName("내가 속해 있는 채팅방을 찾을 수 있다.")
    @Test
    void findMine() {
        // when
        FindMyChatRoomResponse response = chatRoomQueryService.findMine(member1.getId());

        // then
        assertAll(
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetail::chatRoomId)
                        .containsExactly(chatRoom1.getId(), chatRoom2.getId()),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetail::clubName)
                        .containsExactly("모임 제목1", "모임 제목2"),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetail::memberCount)
                        .containsExactly(1, 1),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetail::clubImageUrl)
                        .containsExactly("https://image.com", "https://image.com")
        );
    }

    @DisplayName("참여중인 1대1 채팅방과 최근 메시지를 조회한다.")
    @Transactional
    @Test
    void findMineV2_PrivateChat() {
        // given
        ChatRoom chatRoom1 = chatRoomRepository.save(ChatRoom.createPrivate(member1, member2));
        ChatRoom chatRoom2 = chatRoomRepository.save(ChatRoom.createPrivate(member2, member3));
        chatRoom2.removeMember(member3);

        jdbcTemplate.update("""
                        INSERT INTO chat_message (chat_room_id, message_type, member_id, content, created_at)
                        VALUES
                        (?, 'CHAT', ?, '1번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '1번방 최근 메시지', '2024-01-01T01:00:00'),

                        (?, 'CHAT', ?, '2번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '2번방 최근 메시지', '2024-01-01T02:00:00')
                        """,
                chatRoom1.getId(), member1.getId(),
                chatRoom1.getId(), member1.getId(),
                chatRoom2.getId(), member2.getId(),
                chatRoom2.getId(), member2.getId()
        );

        // when
        FindMyChatRoomResponseV2 response = chatRoomQueryService.findMineV2(member2.getId());

        // then
        assertAll(
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::chatRoomId)
                        .containsExactly(chatRoom1.getId(), chatRoom2.getId()),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::title)
                        .containsExactly("트레", null),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::memberCount)
                        .containsExactly(2, 1),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::imageUrl)
                        .containsExactly("https://img1.com/image.jpg", null),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::recentMessage)
                        .containsExactly("1번방 최근 메시지", "2번방 최근 메시지"),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::recentMessageCreatedAt)
                        .containsExactly(
                                LocalDateTime.parse("2024-01-01T01:00:00"),
                                LocalDateTime.parse("2024-01-01T02:00:00"))
        );
    }

    @DisplayName("참여중인 모임 채팅방과, 각각의 최근 메시지를 조회한다.")
    @Transactional
    @Test
    void findMineV2() {
        // given (1): member3이 chatRoom1, chatRoom2에 참여
        club1.addClubMember(member3);
        club1.addClubPet(List.of(pet3));
        club1.addChatRoomMember(member3);

        club2.addClubMember(member3);
        club2.addClubPet(List.of(pet3));
        club2.addChatRoomMember(member3);

        // given (2): chatRoom1, chatRoom2에서 채팅 발생
        jdbcTemplate.update("""
                        INSERT INTO chat_message (chat_room_id, message_type, member_id, content, created_at)
                        VALUES
                        (?, 'CHAT', ?, '1번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '1번방 최근 메시지', '2024-01-01T01:00:00'),
                        (?, 'CHAT', ?, '1번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '1번방 예전 메시지', '2024-01-01T00:00:00'),

                        (?, 'CHAT', ?, '2번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '2번방 예전 메시지', '2024-01-01T00:00:00'),
                        (?, 'CHAT', ?, '2번방 최근 메시지', '2024-01-01T02:00:00'),
                        (?, 'CHAT', ?, '2번방 예전 메시지', '2024-01-01T00:00:00');
                        """,
                chatRoom1.getId(), member1.getId(),
                chatRoom1.getId(), member1.getId(),
                chatRoom1.getId(), member1.getId(),
                chatRoom1.getId(), member1.getId(),
                chatRoom2.getId(), member1.getId(),
                chatRoom2.getId(), member1.getId(),
                chatRoom2.getId(), member1.getId(),
                chatRoom2.getId(), member1.getId()
        );

        // when
        FindMyChatRoomResponseV2 response = chatRoomQueryService.findMineV2(member3.getId());

        // then
        assertAll(
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::chatRoomId)
                        .containsExactly(chatRoom1.getId(), chatRoom2.getId()),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::title)
                        .containsExactly("모임 제목1", "모임 제목2"),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::memberCount)
                        .containsExactly(2, 2),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::imageUrl)
                        .containsExactly("https://image.com", "https://image.com"),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::recentMessage)
                        .containsExactly("1번방 최근 메시지", "2번방 최근 메시지"),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetailV2::recentMessageCreatedAt)
                        .containsExactly(
                                LocalDateTime.parse("2024-01-01T01:00:00"),
                                LocalDateTime.parse("2024-01-01T02:00:00"))
        );
    }

    @DisplayName("채팅방 내 멤버 세부정보를 조회할 수 있다.")
    @Transactional
    @Test
    void findMemberInfo() {
        // given
        club1.addClubMember(member2);
        club1.addClubPet(List.of(pet2));
        club1.addChatRoomMember(member2);

        // when
        List<FindChatRoomMembersInfoResponse> response
                = chatRoomQueryService.findMemberInfo(member1.getId(), chatRoom1.getId());

        // then
        assertAll(
                () -> assertThat(response)
                        .extracting(FindChatRoomMembersInfoResponse::memberName)
                        .containsExactly("트레", "벼리"),
                () -> assertThat(response)
                        .extracting(FindChatRoomMembersInfoResponse::isOwner)
                        .containsExactly(true, false)
        );
    }

    @DisplayName("채팅방 ID로부터 모임 ID, 허용 펫 사이즈, 허용 펫 성별을 조회할 수 있다.")
    @Transactional
    @Test
    void findClubDetails() {
        // when
        FindClubDetailsResponse response = chatRoomQueryService.findClubDetails(member1.getId(), chatRoom1.getId());

        // then
        assertAll(
                () -> assertThat(response.clubId())
                        .isEqualTo(club1.getId()),
                () -> assertThat(response.allowedSizeTypes())
                        .containsExactlyInAnyOrder(SMALL, MEDIUM, LARGE),
                () -> assertThat(response.allowedGenders())
                        .containsExactlyInAnyOrder(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)
        );
    }

    @DisplayName("자신이 참여한 채팅방이 아니면 채팅방에서 모임 정보를 받아올 수 없다.")
    @Test
    void findClubDetails_Fail() {
        // when - then
        assertThatThrownBy(() -> chatRoomQueryService.findClubDetails(member3.getId(), chatRoom1.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("채팅방에 참여해 있지 않습니다.");
    }
}
