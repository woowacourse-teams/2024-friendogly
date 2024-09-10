package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatCommandServiceTest {

    @Autowired
    private ChatCommandService chatCommandService;

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
        chatCommandService.enter(otherMember.getId(), chatRoom.getId());

        // then
        assertThat(chatRoom.findMembers()).extracting(Member::getId)
                .containsExactly(member.getId(), otherMember.getId());
    }

    @DisplayName("채팅방에서 퇴장한다.")
    @Transactional
    @Test
    void leave() {
        // when
        chatCommandService.leave(member.getId(), chatRoom.getId());

        // then
        assertThat(chatRoom.isEmpty()).isTrue();
    }

    @DisplayName("채팅 입장 메시지를 DB에 저장한다.")
    @Test
    void enter_SaveDatabase() {
        // when
        chatCommandService.enter(member.getId(), chatRoom.getId());

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
        chatCommandService.sendChat(member.getId(), chatRoom.getId(), request);

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
        chatCommandService.leave(member.getId(), chatRoom.getId());

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
