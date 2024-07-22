package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class FootprintCommandServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @DisplayName("발자국 저장")
    @Test
    void save() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        footprintCommandService.save(new SaveFootprintRequest(member.getId(), 30.0, 30.0));
        assertThat(footprintRepository.findAll()).hasSize(1);
    }

    @DisplayName("발자국 저장 실패 - 존재하지 않는 Member ID")
    @Test
    void save_Fail_IllegalMemberId() {
        assertThatThrownBy(
            () -> footprintCommandService.save(new SaveFootprintRequest(1L, 30.0, 30.0))
        ).isInstanceOf(FriendoglyException.class)
            .hasMessage("존재하지 않는 사용자 ID입니다.");
    }

    @DisplayName("발자국 저장 실패 - 30초 전에 이미 발자국을 남긴 경우")
    @Test
    void save_Fail_TooOftenSave() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        jdbcTemplate.update("""
            INSERT INTO footprint (member_id, latitude, longitude, created_at, is_deleted)
            VALUES
            (?, 0.00000, 0.00000, TIMESTAMPADD(SECOND, -29, NOW()), FALSE)
            """, member.getId());

        assertThatThrownBy(
            () -> footprintCommandService.save(new SaveFootprintRequest(member.getId(), 30.0, 30.0))
        ).isInstanceOf(FriendoglyException.class)
            .hasMessage("마지막 발자국을 찍은 뒤 30초가 경과되지 않았습니다.");
    }
}
