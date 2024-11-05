package com.happy.friendogly.chatsocket.service;

import static com.happy.friendogly.chatsocket.domain.MessageType.ENTER;
import static com.happy.friendogly.chatsocket.domain.MessageType.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chatmessage.domain.ChatMessage;
import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatsocket.dto.request.ChatMessageSocketRequest;
import com.happy.friendogly.club.domain.Club;
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

class ChatSocketCommandServiceTest extends ServiceTest {

    @Autowired
    private ChatSocketCommandService chatSocketCommandService;

    private Member member;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("트레", "abcdef12", "https://image.com/image.jpg"));
        Pet pet = petRepository.save(
                new Pet(member, "asda", "asfdfdsa", LocalDate.now().minusYears(1), SizeType.SMALL, Gender.MALE,
                        "https://image.com"));
        Club club = clubRepository.save(Club.create(
                "모임", "모임입니다.", "서울특별시", "구구구", "동동동",
                5, member, Set.of(Gender.MALE), Set.of(SizeType.SMALL), "https://image.com", List.of(pet)
        ));
        chatRoom = club.getChatRoom();
    }

    @DisplayName("채팅 입장 메시지를 DB에 저장한다.")
    @Test
    void enter_SaveDatabase() {
        // when
        chatSocketCommandService.sendEnter(member.getId(), chatRoom.getId());

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
        chatSocketCommandService.sendChat(member.getId(), chatRoom.getId(), request);

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
        chatSocketCommandService.sendLeave(member.getId(), chatRoom.getId());

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
