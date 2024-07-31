package com.woowacourse.friendogly.footprint.domain;

import static com.woowacourse.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.ONGOING;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.friendogly.member.domain.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FootPrintTest {

    private Member member = Member.builder()
            .name("땡이 주인")
            .email("ddang@email.com")
            .build();

    @DisplayName("산책이전일경우,산책 상태 변경된 시간은 발자국 생성시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsBefore(){
        // given
        LocalDateTime expectedTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = Footprint.builder()
                .member(member)
                .location(new Location(30, 30))
                .walkStatus(BEFORE)
                .createdAt(expectedTime)
                .build();

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedTime);
    }

    @DisplayName("산책중일경우, 산책 상태 변경된 시간은 산책 시작 시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsOngoing(){
        // given
        LocalDateTime expectedTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = Footprint.builder()
                .member(member)
                .location(new Location(30, 30))
                .walkStatus(ONGOING)
                .startWalkTime(expectedTime)
                .createdAt(LocalDateTime.of(2023, 12, 12, 9, 30))
                .build();

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedTime);
    }

    @DisplayName("산책후일경우, 산책 상태 변경된 시간은 산책 종료 시간이 반환된다.")
    @Test
    void findChangedWalkStatusTime_when_WalkStatusIsAfter(){
        // given
        LocalDateTime expectedTime = LocalDateTime.of(2023, 12, 12, 10, 30);
        Footprint footprint = Footprint.builder()
                .member(member)
                .location(new Location(30, 30))
                .walkStatus(AFTER)
                .endWalkTime(expectedTime)
                .createdAt(LocalDateTime.of(2023, 12, 12, 9, 30))
                .build();

        // when
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();

        // then
        assertThat(changedWalkStatusTime).isEqualTo(expectedTime);
    }
}
