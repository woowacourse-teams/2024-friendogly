package com.happy.friendogly.support;

import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.footprint.repository.FootprintRepository;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import com.happy.friendogly.pet.repository.PetRepository;
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
    protected DeviceTokenRepository deviceTokenRepository;

    @BeforeAll
    static void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @BeforeEach
    void clearDB() {
        clubRepository.deleteAll();
        footprintRepository.deleteAll();
        petRepository.deleteAll();
        deviceTokenRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
