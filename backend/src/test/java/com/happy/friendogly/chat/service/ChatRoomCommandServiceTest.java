package com.happy.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatRoomCommandServiceTest extends ServiceTest {

    @Autowired
    private ChatRoomCommandService chatRoomCommandService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

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
}
