package com.happy.friendogly.footprint.domain;

import static com.happy.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.happy.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.happy.friendogly.footprint.domain.WalkStatus.ONGOING;
import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.member.domain.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FootPrintTest {

    private Member member = Member.builder()
            .name("땡이 주인")
            .build();

    @DisplayName("산책이전일경우,산책 상태 변경된 시간은 발자국 생성시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsBefore(){
        // given
        LocalDateTime expectedChangedWalkStatusTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = new Footprint(
                member,
                new Location(30, 30),
                BEFORE,
                null,
                null,
                expectedChangedWalkStatusTime,
                false
                );

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedChangedWalkStatusTime);
    }

    @DisplayName("산책중일경우, 산책 상태 변경된 시간은 산책 시작 시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsOngoing(){
        // given
        LocalDateTime expectedChangedWalkStatusTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = new Footprint(
                member,
                new Location(30, 30),
                ONGOING,
                expectedChangedWalkStatusTime,
                null,
                expectedChangedWalkStatusTime.minusHours(2),
                false
        );

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedChangedWalkStatusTime);
    }

    @DisplayName("산책후일경우, 산책 상태 변경된 시간은 산책 종료 시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsAfter(){
        // given
        LocalDateTime expectedChangedWalkStatusTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = new Footprint(
                member,
                new Location(30, 30),
                AFTER,
                expectedChangedWalkStatusTime.minusHours(1),
                expectedChangedWalkStatusTime,
                expectedChangedWalkStatusTime.minusHours(2),
                false
        );

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedChangedWalkStatusTime);
    }
}
