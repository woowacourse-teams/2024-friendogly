package com.happy.friendogly.chat.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ChatRoomServiceTest extends ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    protected Member member1;

    protected Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(
                Member.builder()
                        .name("트레")
                        .tag("123abc")
                        .email("tre@test.com")
                        .imageUrl("https://picsum.photos/100")
                        .build()
        );

        member2 = memberRepository.save(
                Member.builder()
                        .name("땡이")
                        .tag("qwerty")
                        .email("ddang2@test.com")
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );
    }
}
