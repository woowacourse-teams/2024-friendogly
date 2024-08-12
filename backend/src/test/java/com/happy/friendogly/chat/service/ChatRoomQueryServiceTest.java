package com.happy.friendogly.chat.service;

import static com.happy.friendogly.pet.domain.Gender.FEMALE;
import static com.happy.friendogly.pet.domain.Gender.FEMALE_NEUTERED;
import static com.happy.friendogly.pet.domain.Gender.MALE;
import static com.happy.friendogly.pet.domain.Gender.MALE_NEUTERED;
import static com.happy.friendogly.pet.domain.SizeType.LARGE;
import static com.happy.friendogly.pet.domain.SizeType.MEDIUM;
import static com.happy.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.response.ChatRoomDetail;
import com.happy.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chat.dto.response.FindMyChatRoomResponse;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatRoomQueryServiceTest extends ServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

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
    @Transactional
    void setUp() {
        member1 = memberRepository.save(new Member("name", "a", "a@a.com", "https://a.com"));
        member2 = memberRepository.save(new Member("name2", "b", "b@a.com", "https://b.com"));
        member3 = memberRepository.save(new Member("name3", "c", "c@a.com", "https://c.com"));

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
        club1.addClubMember(member2);
        club1.addClubPet(List.of(pet2));
        club1.addChatRoomMember(member2);
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
        club2.addClubMember(member3);
        club2.addClubPet(List.of(pet3));
        club2.addChatRoomMember(member3);
        clubRepository.save(club2);

        chatRoom1 = club1.getChatRoom();
        chatRoom2 = club2.getChatRoom();
    }

    @DisplayName("내가 속해 있는 채팅방을 찾을 수 있다.")
    @Transactional
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
                        .containsExactly(2, 2),
                () -> assertThat(response.chatRooms())
                        .extracting(ChatRoomDetail::clubImageUrl)
                        .containsExactly("https://image.com", "https://image.com")
        );
    }

    @DisplayName("채팅방 내 멤버 세부정보를 조회할 수 있다.")
    @Transactional
    @Test
    void findMemberInfo() {
        // when
        List<FindChatRoomMembersInfoResponse> response
                = chatRoomQueryService.findMemberInfo(member1.getId(), chatRoom1.getId());

        // then
        assertAll(
                () -> assertThat(response)
                        .extracting(FindChatRoomMembersInfoResponse::memberName)
                        .containsExactly("name", "name2"),
                () -> assertThat(response)
                        .extracting(FindChatRoomMembersInfoResponse::isOwner)
                        .containsExactly(true, false)
        );
    }
}
