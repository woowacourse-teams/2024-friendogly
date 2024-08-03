package com.woowacourse.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.support.ServiceTest;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("name", "a", "a@a.com", "https://a.com"));
        member2 = memberRepository.save(new Member("name2", "b", "b@a.com", "https://b.com"));
        member3 = memberRepository.save(new Member("name3", "c", "c@a.com", "https://c.com"));

        chatRoom1 = chatRoomRepository.save(new ChatRoom());
        chatRoom2 = chatRoomRepository.save(new ChatRoom());
    }

    @DisplayName("내가 속해 있는 채팅방을 찾을 수 있다.")
    @Transactional
    @Test
    void findMine() {
        // given
        chatRoom1.addMember(member1);
        chatRoom1.addMember(member2);

        chatRoom2.addMember(member1);
        chatRoom2.addMember(member3);

        // when
        List<FindMyChatRoomResponse> response = chatRoomQueryService.findMine(member1.getId());

        // then
        assertAll(
                () -> assertThat(response)
                        .extracting(FindMyChatRoomResponse::chatRoomId)
                        .containsExactly(chatRoom1.getId(), chatRoom2.getId()),
                () -> assertThat(response)
                        .extracting(FindMyChatRoomResponse::memberNames)
                        .containsExactly(List.of("name", "name2"), List.of("name", "name3")),
                () -> assertThat(response)
                        .extracting(FindMyChatRoomResponse::memberCount)
                        .containsExactly(2, 2)
        );
    }

    @DisplayName("채팅방 내 멤버 세부정보를 조회할 수 있다.")
    @Transactional
    @Test
    void findMemberInfo() {
        // given
        chatRoom1.addMember(member1);
        chatRoom1.addMember(member2);
        chatRoom1.addMember(member3);

        // when
        List<FindChatRoomMembersInfoResponse> response = chatRoomQueryService.findMemberInfo(chatRoom1.getId());

        // then
        assertThat(response)
                .extracting(FindChatRoomMembersInfoResponse::memberName)
                .containsExactly("name", "name2", "name3");
    }
}
