package com.happy.friendogly.playground.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaygroundQueryServiceTest extends PlaygroundServiceTest {

    @Autowired
    private PlaygroundQueryService playgroundQueryService;

    @DisplayName("놀이터 상세정보를 조회한다.")
    @Test
    void findPlaygroundDetail() {
        // given
        Member member1 = saveMember("member1");
        Member member2 = saveMember("member2");
        savePet(member1);
        savePet(member1);
        savePet(member1);
        savePet(member2);
        Playground playground = savePlayground();
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member1,
                        "message",
                        true,
                        null
                )
        );
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member2,
                        "message",
                        false,
                        null
                )
        );

        // when
        FindPlaygroundDetailResponse response = playgroundQueryService.findDetail(member1.getId(), playground.getId());

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(playground.getId()),
                () -> assertThat(response.totalPetCount()).isEqualTo(4),
                () -> assertThat(response.arrivedPetCount()).isEqualTo(3),
                () -> assertThat(response.playgroundPetDetails().size()).isEqualTo(4)
        );
    }

    @DisplayName("놀이터들의 위치를 조회한다.")
    @Test
    void findLocations() {
        // given
        double expectedLatitude = 37.5173316;
        double expectedLongitude = 127.1011661;
        Playground playground = savePlayground(expectedLatitude, expectedLongitude);

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(1L);

        // then
        assertAll(
                () -> assertThat(response.get(0).latitude()).isEqualTo(expectedLatitude),
                () -> assertThat(response.get(0).longitude()).isEqualTo(expectedLongitude)
        );
    }

    @DisplayName("놀이터들의 위치를 조회할 때, 내가 참여했는 지 알 수있다(true)")
    @Test
    void findLocationsWithIsParticipatingTrue() {
        // given
        Member member1 = saveMember("member1");
        savePet(member1);

        Playground playground = savePlayground();
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member1,
                        "message",
                        false,
                        null
                )
        );

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(member1.getId());

        // then
        assertThat(response.get(0).isParticipating()).isEqualTo(true);
    }

    @DisplayName("놀이터들의 위치를 조회할 때, 내가 참여했는 지 알 수있다(false)")
    @Test
    void findLocationsWithIsParticipatingFalse() {
        // given
        Member member1 = saveMember("member1");
        savePet(member1);

        savePlayground();

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(member1.getId());

        // then
        assertThat(response.get(0).isParticipating()).isEqualTo(false);
    }
}
