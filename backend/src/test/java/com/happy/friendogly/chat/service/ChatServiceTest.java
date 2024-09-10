package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatMessage;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

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

    @DisplayName("채팅방에 입장한다.")
    @Transactional
    @Test
    void enter() {
        // given
        Member otherMember = memberRepository.save(new Member("땡이", "aaa111bc", "https://image.com/image.jpg"));

        // when
        chatService.enter(otherMember.getId(), chatRoom.getId());

        // then
        assertThat(chatRoom.findMembers()).extracting(Member::getId)
                .containsExactly(member.getId(), otherMember.getId());
    }

    @DisplayName("채팅방에서 퇴장한다.")
    @Transactional
    @Test
    void leave() {
        // when
        chatService.leave(member.getId(), chatRoom.getId());

        // then
        assertThat(chatRoom.isEmpty()).isTrue();
    }

    @DisplayName("채팅방 ID로 채팅 메시지를 모두 찾을 수 있다.")
    @Transactional
    @Test
    void findAllByChatRoomId() {
        // given
        Member otherMember = memberRepository.save(new Member("땡이", "aaa111bc", "https://image.com/image.jpg"));

        chatService.enter(otherMember.getId(), chatRoom.getId());
        chatService.sendChat(otherMember.getId(), chatRoom.getId(), new ChatMessageRequest("안녕하세요!"));
        chatService.sendChat(member.getId(), chatRoom.getId(), new ChatMessageRequest("반가워요!"));
        chatService.sendChat(otherMember.getId(), chatRoom.getId(), new ChatMessageRequest("이만 나가볼게요!"));
        chatService.leave(otherMember.getId(), chatRoom.getId());

        // when
        List<FindChatMessagesResponse> messages = chatService.findAllByChatRoomId(member.getId(), chatRoom.getId());

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
        assertThatThrownBy(() -> chatService.findAllByChatRoomId(otherMember.getId(), chatRoom.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("채팅 내역을 조회할 수 있는 권한이 없습니다.");
    }

    @DisplayName("채팅 입장 메시지를 DB에 저장한다.")
    @Test
    void enter_SaveDatabase() {
        // when
        chatService.enter(member.getId(), chatRoom.getId());

        // then
        List<ChatMessage> messages = chatMessageRepository.findAll();
        assertAll(
                () -> assertThat(messages).extracting(ChatMessage::getMessageType)
                        .containsExactly(ENTER),
                () -> assertThat(messages).extracting(ChatMessage::getContent)
                        .containsExactly("")
        );
    }

    @DisplayName("채팅 메시지를 DB에 저장한다.")
    @Transactional
    @Test
    void send_SaveDatabase() {
        // given
        ChatMessageRequest request = new ChatMessageRequest("반갑습니다.");

        // when
        chatService.sendChat(member.getId(), chatRoom.getId(), request);

        // then
        List<ChatMessage> messages = chatMessageRepository.findAll();
        assertThat(messages).extracting(ChatMessage::getContent)
                .containsExactly("반갑습니다.");
    }

    @DisplayName("채팅 퇴장 메시지를 DB에 저장한다.")
    @Transactional
    @Test
    void leave_SaveDatabase() {
        // when
        chatService.leave(member.getId(), chatRoom.getId());

        // then
        List<ChatMessage> messages = chatMessageRepository.findAll();
        assertAll(
                () -> assertThat(messages).extracting(ChatMessage::getMessageType)
                        .containsExactly(LEAVE),
                () -> assertThat(messages).extracting(ChatMessage::getContent)
                        .containsExactly("")
        );
    }
}
