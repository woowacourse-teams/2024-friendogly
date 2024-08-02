package com.woowacourse.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.support.ServiceTest;
import java.util.List;
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

    @DisplayName("내가 속해 있는 채팅방을 찾을 수 있다.")
    @Transactional
    @Test
    void findMine() {
        // given
        Member member = memberRepository.save(new Member("name", "a", "a@a.com", "https://a.com"));
        Member member2 = memberRepository.save(new Member("name2", "b", "b@a.com", "https://b.com"));
        Member member3 = memberRepository.save(new Member("name3", "c", "c@a.com", "https://c.com"));

        ChatRoom chatRoom1 = chatRoomRepository.save(new ChatRoom());
        ChatRoom chatRoom2 = chatRoomRepository.save(new ChatRoom());

        chatRoom1.addMember(member);
        chatRoom1.addMember(member2);
        chatRoom2.addMember(member);
        chatRoom2.addMember(member3);

        // when - then
        assertThat(chatRoomQueryService.findMine(member.getId()))
                .extracting(FindMyChatRoomResponse::chatRoomId)
                .containsExactly(chatRoom1.getId(), chatRoom2.getId());
        assertThat(chatRoomQueryService.findMine(member.getId()))
                .extracting(FindMyChatRoomResponse::memberNames)
                .containsExactly(List.of("name", "name2"), List.of("name", "name3"));
    }
}
