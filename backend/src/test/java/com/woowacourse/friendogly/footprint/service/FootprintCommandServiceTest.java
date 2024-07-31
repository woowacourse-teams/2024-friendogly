package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FootprintCommandServiceTest extends FootprintServiceTest {

    @DisplayName("발자국 저장")
    @Test
    void save() {
        // when
        footprintCommandService.save(member.getId(), new SaveFootprintRequest(30.0, 30.0));

        // then
        assertThat(footprintRepository.findAll()).hasSize(1);
    }

    @DisplayName("강아지를 등록하지 않은 사용자는 발자국을 남길 수 없다.")
    @Test
    void save_Fail_NoPets() {
        // given
        Member memberWithoutPet = memberRepository.save(
                Member.builder()
                        .name("강아지없어요")
                        .email("no_dog@test.com")
                        .build()
        );

        // when - then
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        memberWithoutPet.getId(),
                        new SaveFootprintRequest(90.000, 90.000)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("펫을 등록해야만 발자국을 생성할 수 있습니다.");
    }

    @DisplayName("발자국 저장 실패 - 존재하지 않는 Member ID")
    @Test
    void save_Fail_IllegalMemberId() {
        // when - then
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        -1L,
                        new SaveFootprintRequest(30.0, 30.0)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("존재하지 않는 사용자 ID입니다.");
    }

    @DisplayName("발자국 저장 실패 - 30초 전에 이미 발자국을 남긴 경우")
    @Test
    void save_Fail_TooOftenSave() {
        // given
        jdbcTemplate.update("""
                INSERT INTO footprint (member_id, latitude, longitude, walk_status, created_at, is_deleted)
                VALUES
                (?, 0.00000, 0.00000, 'BEFORE', TIMESTAMPADD(SECOND, -29, NOW()), FALSE)
                """, member.getId());

        // when - then
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        member.getId(),
                        new SaveFootprintRequest(30.0, 30.0)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("마지막 발자국을 찍은 뒤 30초가 경과되지 않았습니다.");
    }
}
