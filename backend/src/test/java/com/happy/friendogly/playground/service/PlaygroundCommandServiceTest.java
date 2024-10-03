package com.happy.friendogly.playground.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaygroundCommandServiceTest extends PlaygroundServiceTest {

    @Autowired
    PlaygroundCommandService playgroundCommandService;

    @DisplayName("놀이터를 등록할 수 있다.")
    @Test
    void save() {
        // given
        Member member = saveMember("김도선");
        SavePlaygroundRequest request = new SavePlaygroundRequest(37.5173316, 127.1011661);

        // when
        SavePlaygroundResponse response = playgroundCommandService.save(request, member.getId());

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(1), // todo: 테스트 격리 안되서 id가 틀릴 위험 있음
                () -> assertThat(response.latitude()).isEqualTo(request.latitude()),
                () -> assertThat(response.longitude()).isEqualTo(request.longitude())
        );
    }
    // 이미 참여한 놀이터가 있다.
    // 겹치는 범위안에 놀이터가 있다.

    // 등록하면 자동으로 참가
}
