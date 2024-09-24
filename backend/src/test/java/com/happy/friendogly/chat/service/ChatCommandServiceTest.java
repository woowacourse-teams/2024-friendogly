package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.ChatMessageSocketRequest;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.support.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatCommandServiceTest extends ServiceTest {

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

    @DisplayName("채팅 입장 메시지를 DB에 저장한다.")
    @Test
    void enter_SaveDatabase() {
        // when
        chatCommandService.sendEnter(member.getId(), chatRoom.getId());

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
        ChatMessageSocketRequest request = new ChatMessageSocketRequest("반갑습니다.");

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
        chatCommandService.sendLeave(member.getId(), chatRoom.getId());

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
