package com.happy.friendogly.footprint.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.Location;
import com.happy.friendogly.footprint.domain.WalkStatus;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FootprintRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FootprintRepository footprintRepository;


    @DisplayName("삭제되지않고, 원하는 시간이내의 발자국만 조회 된다.")
    @Test
    void findAllByIsDeletedFalseAndCreatedAtAfter() {
        Member member1 = memberRepository.save(new Member("name1", "tag1", "http://image.jpg"));
        Member member2 = memberRepository.save(new Member("name2", "tag2", "http://image.jpg"));

        footprintRepository.save(new Footprint(
                member1,
                new Location(0, 0),
                WalkStatus.BEFORE,
                null,
                null,
                LocalDateTime.now().minusHours(25),
                false
        ));

        footprintRepository.save(new Footprint(
                member2,
                new Location(0, 0),
                WalkStatus.BEFORE,
                null,
                null,
                LocalDateTime.now().minusHours(23),
                false
        ));

        footprintRepository.save(new Footprint(
                member2,
                new Location(0, 0),
                WalkStatus.BEFORE,
                null,
                null,
                LocalDateTime.now().minusHours(23),
                true
        ));

        int size = footprintRepository.findAllByIsDeletedFalseAndCreatedAtAfter(LocalDateTime.now().minusHours(24))
                .size();

        assertThat(size).isEqualTo(1);
    }
}
