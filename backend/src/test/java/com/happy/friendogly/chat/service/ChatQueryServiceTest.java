package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatQueryServiceTest {

    @Autowired
    private ChatQueryService chatQueryService;

    @Autowired
    private ChatCommandService chatCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("트레", "abcdef12", "https://image.com/image.jpg"));
        chatRoom = chatRoomRepository.save(ChatRoom.createGroup(5));
        chatRoom.addMember(member);
    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAll();
        chatRoomRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("채팅방 ID로 채팅 메시지를 모두 찾을 수 있다.")
    @Transactional
    @Test
    void findAllByChatRoomId() {
        // given
        Member otherMember = memberRepository.save(new Member("땡이", "aaa111bc", "https://image.com/image.jpg"));

        chatCommandService.enter(otherMember.getId(), chatRoom.getId());
        chatCommandService.sendChat(otherMember.getId(), chatRoom.getId(), new ChatMessageRequest("안녕하세요!"));
        chatCommandService.sendChat(member.getId(), chatRoom.getId(), new ChatMessageRequest("반가워요!"));
        chatCommandService.sendChat(otherMember.getId(), chatRoom.getId(), new ChatMessageRequest("이만 나가볼게요!"));
        chatCommandService.leave(otherMember.getId(), chatRoom.getId());

        // when
        List<FindChatMessagesResponse> messages = chatQueryService.findAllByChatRoomId(
                member.getId(), chatRoom.getId());

        // then
        Long memberId = member.getId();
        Long otherMemberId = otherMember.getId();

        assertAll(
                () -> assertThat(messages).extracting(FindChatMessagesResponse::messageType)
                        .containsExactly(ENTER, CHAT, CHAT, CHAT, LEAVE),
                () -> assertThat(messages).extracting(FindChatMessagesResponse::senderMemberId)
                        .containsExactly(otherMemberId, otherMemberId, memberId, otherMemberId, otherMemberId),
                () -> assertThat(messages).extracting(FindChatMessagesResponse::senderName)
                        .containsExactly("땡이", "땡이", "트레", "땡이", "땡이"),
                () -> assertThat(messages).extracting(FindChatMessagesResponse::content)
                        .containsExactly("", "안녕하세요!", "반가워요!", "이만 나가볼게요!", ""),
                () -> assertThat(messages).extracting(FindChatMessagesResponse::createdAt)
                        .hasOnlyElementsOfType(LocalDateTime.class)
                        .doesNotContainNull(),
                () -> assertThat(messages).extracting(FindChatMessagesResponse::profilePictureUrl)
                        .containsExactly(
                                "https://image.com/image.jpg",
                                "https://image.com/image.jpg",
                                "https://image.com/image.jpg",
                                "https://image.com/image.jpg",
                                "https://image.com/image.jpg"
                        )
        );
    }

    @DisplayName("채팅방에 들어가 있지 않은 Member는 채팅 내역을 조회할 수 없다.")
    @Test
    void findAllByChatRoomId_Fail_Unauthorized() {
        // given
        Member otherMember = memberRepository.save(new Member("땡이", "aaa111bc", "https://image.com/image.jpg"));

        // when - then
        assertThatThrownBy(() -> chatQueryService.findAllByChatRoomId(otherMember.getId(), chatRoom.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("채팅 내역을 조회할 수 있는 권한이 없습니다.");
    }

    @DisplayName("특정 시점보다 미래의 채팅 내역을 조회할 수 있다.")
    @Transactional
    @Test
    void findRecent() {
        // given
        jdbcTemplate.update("""
                        INSERT INTO chat_message (chat_room_id, message_type, member_id, content, created_at)
                        VALUES
                        (?, 'CHAT', ?, '안녕하세요', ?),
                        (?, 'CHAT', ?, '감사합니다', ?),
                        (?, 'CHAT', ?, '반갑습니다', ?),
                        (?, 'CHAT', ?, '환영합니다', ?);
                        """,
                chatRoom.getId(), member.getId(), LocalDateTime.parse("2024-01-01T10:00:00"),
                chatRoom.getId(), member.getId(), LocalDateTime.parse("2024-01-01T11:00:00"),
                chatRoom.getId(), member.getId(), LocalDateTime.parse("2024-01-01T12:00:00"),
                chatRoom.getId(), member.getId(), LocalDateTime.parse("2024-01-01T13:00:00")
        );

        // when
        List<FindChatMessagesResponse> response = chatQueryService.findRecent(
                member.getId(), chatRoom.getId(), LocalDateTime.parse("2024-01-01T11:00:00"));

        // then
        assertThat(response).extracting(FindChatMessagesResponse::content)
                .containsExactly("반갑습니다", "환영합니다");
    }
}
