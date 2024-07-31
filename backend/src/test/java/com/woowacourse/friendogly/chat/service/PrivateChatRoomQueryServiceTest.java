package com.woowacourse.friendogly.chat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.FindMyPrivateChatRoomResponse;
import com.woowacourse.friendogly.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PrivateChatRoomQueryServiceTest extends ChatRoomServiceTest {

    @Autowired
    private PrivateChatRoomQueryService privateChatRoomQueryService;

    @DisplayName("내가 속해 있는 1:1 채팅방을 찾을 수 있다.")
    @Test
    void findMine() {
        // given
        Member member3 = memberRepository.save(
                new Member("땡이", "aa99", "a@test.com", "https://picsum.photos/200"));

        PrivateChatRoom r1 = privateChatRoomRepository.save(new PrivateChatRoom(member1, member2));
        PrivateChatRoom r2 = privateChatRoomRepository.save(new PrivateChatRoom(member1, member3));

        // when - then
        assertThat(privateChatRoomQueryService.findMine(member1.getId()))
                .extracting(FindMyPrivateChatRoomResponse::privateChatRoomId)
                .containsExactly(r1.getId(), r2.getId());
    }
}
