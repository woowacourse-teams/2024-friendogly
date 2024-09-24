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
import org.springframework.jdbc.core.JdbcTemplate;

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

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @BeforeEach
    void clearDB() {
        jdbcTemplate.update("""
                SET REFERENTIAL_INTEGRITY FALSE;
                                
                DELETE FROM chat_room;
                DELETE FROM chat_room_member;
                DELETE FROM club;
                DELETE FROM club_gender;
                DELETE FROM club_member;
                DELETE FROM club_pet;
                DELETE FROM club_size;
                DELETE FROM device_token;
                DELETE FROM footprint;
                DELETE FROM kakao_member;
                DELETE FROM member;
                DELETE FROM pet;
                                
                SET REFERENTIAL_INTEGRITY TRUE;
                """);
    }
}
