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

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatQueryServiceTest extends ServiceTest {

    @Autowired
    private ChatQueryService chatQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member;
    private ChatRoom chatRoom;
    private Club club;
    private Pet pet;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("트레", "abcdef12", "https://image.com/image.jpg"));

        pet = petRepository.save(
                new Pet(member, "강아지", "귀여워요", LocalDate.of(2020, 1, 1), SMALL, MALE, "https://image.com"));

        club = clubRepository.save(Club.create("서울 강아지 모임", "매주 월요일에 모여요", "서울특별시", "서초구", "서초동", 5, member,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED), Set.of(SMALL, MEDIUM, LARGE), "https://image.com",
                List.of(pet)));

        chatRoom = club.getChatRoom();
    }

    @DisplayName("채팅방에 들어가 있지 않은 Member는 채팅 내역을 조회할 수 없다.")
    @Test
    void findAllByChatRoomId_Fail_Unauthorized() {
        // given
        Member otherMember = memberRepository.save(new Member("땡이", "aaa111bc", "https://image.com/image.jpg"));

        // when - then
        assertThatThrownBy(
                () -> chatQueryService.findAllByChatRoomId(otherMember.getId(), chatRoom.getId())).isInstanceOf(
                FriendoglyException.class).hasMessage("채팅 내역을 조회할 수 있는 권한이 없습니다.");
    }

    @DisplayName("since 시간과 until 시간 사이의 (포함 X) 채팅 내역을 조회할 수 있다.")
    @Transactional
    @Test
    void findAllByTimeRange() {
        // given
        jdbcTemplate.update("""
                        INSERT INTO chat_message (chat_room_id, message_type, member_id, content, created_at)
                        VALUES
                        (?, 'CHAT', ?, '안녕하세요', ?),
                        (?, 'CHAT', ?, '감사합니다', ?),
                        (?, 'CHAT', ?, '반갑습니다', ?),
                        (?, 'CHAT', ?, '환영합니다', ?);
                        """, chatRoom.getId(), member.getId(), LocalDateTime.parse("2024-01-01T10:00:00"), chatRoom.getId(),
                member.getId(), LocalDateTime.parse("2024-01-01T11:00:00"), chatRoom.getId(), member.getId(),
                LocalDateTime.parse("2024-01-01T12:00:00"), chatRoom.getId(), member.getId(),
                LocalDateTime.parse("2024-01-01T13:00:00"));

        // when
        FindMessagesByTimeRangeRequest request = new FindMessagesByTimeRangeRequest(
                LocalDateTime.parse("2024-01-01T10:00:00"), LocalDateTime.parse("2024-01-01T13:00:00"));

        List<FindChatMessagesResponse> response = chatQueryService.findByTimeRange(member.getId(), chatRoom.getId(),
                request);

        // then
        assertThat(response).extracting(FindChatMessagesResponse::content).containsExactly("감사합니다", "반갑습니다");
    }

    @DisplayName("채팅 메시지 시간 범위 조회에서, 시간 범위가 잘못된 경우 예외가 발생한다.")
    @Test
    void findAllByTimeRange_Fail_InvalidTimeRange() {
        // given
        FindMessagesByTimeRangeRequest request = new FindMessagesByTimeRangeRequest(
                LocalDateTime.parse("2021-01-01T10:00:00"), LocalDateTime.parse("2021-01-01T09:59:59"));

        // when & then
        assertThatThrownBy(
                () -> chatQueryService.findByTimeRange(member.getId(), chatRoom.getId(), request)).isInstanceOf(
                FriendoglyException.class).hasMessage("since 시간을 until 시간보다 과거로 설정해 주세요.");
    }
}
