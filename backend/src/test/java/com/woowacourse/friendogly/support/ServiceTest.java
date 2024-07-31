package com.woowacourse.friendogly.support;

import com.woowacourse.friendogly.chat.repository.PrivateChatRoomRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected ClubRepository clubRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PetRepository petRepository;

    @Autowired
    protected FootprintRepository footprintRepository;

    @Autowired
    protected PrivateChatRoomRepository privateChatRoomRepository;

    @BeforeAll
    static void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @BeforeEach
    void clearDB() {
        privateChatRoomRepository.deleteAll();
        clubRepository.deleteAll();
        footprintRepository.deleteAll();
        petRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
