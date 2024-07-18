package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class FootprintQueryServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FootprintQueryService footprintQueryService;

    @Autowired
    private FootprintCommandService footprintCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FootprintRepository footprintRepository;

    @AfterEach
    void cleanUp() {
        footprintRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("현재 위치 기준 1km 이내 발자국의 수가 1개일 때, 1개의 발자국만 조회된다.")
    @Test
    void findNear() {
        Member member1 = memberRepository.save(
            Member.builder()
                .name("name1")
                .email("test@test.com")
                .build()
        );

        Member member2 = memberRepository.save(
            Member.builder()
                .name("name2")
                .email("test@test.com")
                .build()
        );

        double nearLongitude = 0.008993216;
        double farLongitude = 0.009001209;

        // 999m 떨어진 발자국 저장
        footprintCommandService.save(new SaveFootprintRequest(member1.getId(), 0.0, nearLongitude));

        // 1001m 떨어진 발자국 저장
        footprintCommandService.save(new SaveFootprintRequest(member2.getId(), 0.0, farLongitude));

        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
            member1.getId(), new FindNearFootprintRequest(0.0, 0.0));

        assertAll(
            () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::latitude)
                .containsExactly(0.0),
            () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::longitude)
                .containsExactly(nearLongitude)
        );
    }

    @DisplayName("현재 시간 기준 24시간 보다 이전에 생성된 발자국은 조회되지 않는다.")
    @Test
    void findNear24Hours() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        jdbcTemplate.update("""
            INSERT INTO footprint (member_id, latitude, longitude, created_at, is_deleted)
            VALUES
            (?, 0.00000, 0.00000, TIMESTAMPADD(HOUR, -25, NOW()), FALSE),
            (?, 0.00000, 0.00000, TIMESTAMPADD(HOUR, -23, NOW()), FALSE),
            (?, 0.00000, 0.00000, TIMESTAMPADD(HOUR, -22, NOW()), FALSE);
            """, member.getId(), member.getId(), member.getId());

        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
            member.getId(), new FindNearFootprintRequest(0.0, 0.0));

        assertAll(
            () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::latitude)
                .containsExactly(0.00000, 0.00000),
            () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::longitude)
                .containsExactly(0.00000, 0.00000)
        );
    }

    @DisplayName("자신이 마지막으로 발자국 찍은 시간 조회 - 찍은 발자국이 있는 경우")
    @Test
    void findMyLatestFootprintTime_MyFootprintExists() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        jdbcTemplate.update("""
            INSERT INTO footprint (member_id, latitude, longitude, created_at, is_deleted)
            VALUES
            (?, 0.00000, 0.00000, TIMESTAMPADD(HOUR, -25, NOW()), FALSE),
            (?, 0.11111, 0.11111, TIMESTAMPADD(HOUR, -23, NOW()), FALSE),
            (?, 0.22222, 0.22222, ?, FALSE);
            """, member.getId(), member.getId(), member.getId(), oneMinuteAgo);

        LocalDateTime time = footprintQueryService.findMyLatestFootprintTime(member.getId()).createdAt();

        assertAll(
            () -> assertThat(time.getHour()).isEqualTo(oneMinuteAgo.getHour()),
            () -> assertThat(time.getMinute()).isEqualTo(oneMinuteAgo.getMinute()),
            () -> assertThat(time.getSecond()).isEqualTo(oneMinuteAgo.getSecond())
        );
    }

    @DisplayName("자신이 마지막으로 발자국 찍은 시간 조회 - 찍은 발자국이 없는 경우")
    @Test
    void findMyLatestFootprintTime_MyFootprintDoesNotExist() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        assertThat(footprintQueryService.findMyLatestFootprintTime(member.getId()))
            .extracting(FindMyLatestFootprintTimeResponse::createdAt)
            .isNull();
    }
}
