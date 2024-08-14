package com.happy.friendogly.footprint.service;

import static com.happy.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.happy.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.happy.friendogly.footprint.domain.WalkStatus.ONGOING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.Location;
import com.happy.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.happy.friendogly.footprint.dto.request.UpdateWalkStatusRequest;
import com.happy.friendogly.member.domain.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

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
        footprintRepository.save(FOOTPRINT(LocalDateTime.now().minusSeconds(29)));

        // when - then
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        member.getId(),
                        new SaveFootprintRequest(30.0, 30.0)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("마지막 발자국을 찍은 뒤 30초가 경과되지 않았습니다.");
    }

    @DisplayName("최근 발자국이 있는 경우 발자국 생성 시 최근 발자국은 삭제된다")
    @Transactional
    @Test
    void save_With_DeleteRecentFootprint() {
        // given
        Footprint recentFootprint = footprintRepository.save(
                FOOTPRINT(LocalDateTime.now().minusMinutes(10))
        );

        // when
        footprintCommandService.save(member.getId(), new SaveFootprintRequest(40, 40));

        // then
        assertThat(recentFootprint.isDeleted()).isTrue();
    }

    @DisplayName("발자국 상태 변경 실패 - 발자국 존재하지 않음")
    @Test
    void updateWalkStatus_fail_nonExistFootprint() {
        // when - then
        assertThatThrownBy(() -> footprintCommandService.updateWalkStatus(
                        member.getId(),
                        new UpdateWalkStatusRequest(0, 0)
                )
        ).isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("발자국이 존재하지 않습니다.");
    }

    @DisplayName("발자국 상태 변경 실패 - 가장 최근 발자국이 삭제된(logical) 발자국")
    @Test
    void updateWalkStatus_fail_logicalDeletedFootprint() {
        // given
        footprintRepository.save(FOOTPRINT_DELETED());

        // when - then
        assertThatThrownBy(() -> footprintCommandService.updateWalkStatus(
                        member.getId(),
                        new UpdateWalkStatusRequest(0, 0)
                )
        ).isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("가장 최근 발자국이 삭제된 상태입니다.");
    }

    @DisplayName("산책 전이고 현재위치가 범위 안이면 산책 중으로 변한다")
    @Transactional
    @Test
    void updateWalkStatus_beforeToOngoing() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_BEFORE(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(999))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(ONGOING);
    }

    @DisplayName("산책 전이고 현재위치가 범위 밖이면 산책 상태 변화는 없다")
    @Transactional
    @Test
    void updateWalkStatus_beforeNoChange() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_BEFORE(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(1001))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(BEFORE);
    }

    @DisplayName("산책 중이고 현재위치가 범위 밖이면 산책 후로 변한다")
    @Transactional
    @Test
    void updateWalkStatus_ongoingToAfter() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_ONGOING(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(1001))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(AFTER);
    }


    @DisplayName("산책 중이고 현재위치가 범위 안이면 산책 상태 변화는 없다")
    @Transactional
    @Test
    void updateWalkStatus_ongoingNoChange() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_ONGOING(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(999))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(ONGOING);
    }


    @DisplayName("산책 후이고, 현재위치가 범위 안이면 산책 상태 변화는 없다")
    @Transactional
    @Test
    void updateWalkStatus_ongoingNoChange_inside() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_AFTER(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(999))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(AFTER);
    }

    @DisplayName("산책 후이고, 현재위치가 범위 밖이면 산책 상태 변화는 없다")
    @Transactional
    @Test
    void updateWalkStatus_afterNoChange_outside() {
        // given
        Footprint savedFootprint = footprintRepository.save(FOOTPRINT_STATUS_AFTER(new Location(0, 0)));

        // when
        footprintCommandService.updateWalkStatus(
                member.getId(),
                new UpdateWalkStatusRequest(0.0, LONGITUDE_WITH_METER_FROM_ZERO(1001))
        );

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(AFTER);
    }

    @DisplayName("산책 전이고, 산책 종료시, 발자국이 삭제된다,")
    @Transactional
    @Test
    void stopWalking_deleteFootprint() {
        // given
        Footprint savedFootprint = footprintRepository.save(
                FOOTPRINT_STATUS_BEFORE(new Location(0, 0))
        );

        // when
        footprintCommandService.stopWalking(member.getId());

        // then
        assertThat(savedFootprint.isDeleted()).isTrue();
    }


    @DisplayName("산책 중이고, 산책 종료시, 산책상태가 산책 후로 변경된다.")
    @Transactional
    @Test
    void stopWalking_changeToAfter() {
        // given
        Footprint savedFootprint = footprintRepository.save(
                FOOTPRINT_STATUS_ONGOING(new Location(0, 0))
        );

        // when
        footprintCommandService.stopWalking(member.getId());

        // then
        assertThat(savedFootprint.getWalkStatus()).isEqualTo(AFTER);
    }
}
