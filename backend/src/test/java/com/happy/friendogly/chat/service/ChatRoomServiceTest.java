package com.happy.friendogly.chat.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;

public abstract class ChatRoomServiceTest extends ServiceTest {

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(
                Member.builder()
                        .name("트레")
                        .tag("123abc")
                        .imageUrl("https://picsum.photos/100")
                        .build()
        );

        member2 = memberRepository.save(
                Member.builder()
                        .name("땡이")
                        .tag("qwerty")
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );
    }
}
