package com.happy.friendogly.playground.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.utils.GeoCalculator;
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
                () -> assertThat(response.latitude()).isEqualTo(request.latitude()),
                () -> assertThat(response.longitude()).isEqualTo(request.longitude())
        );
    }

    @DisplayName("멤버가 놀이터를 등록하면 해당 멤버는 놀이터에 자동으로 참가")
    @Test
    void autoParticipatePlaygroundWhenCreate() {
        // given
        Member member = saveMember("김도선");
        SavePlaygroundRequest request = new SavePlaygroundRequest(37.5173316, 127.1011661);

        // when
        SavePlaygroundResponse response = playgroundCommandService.save(request, member.getId());
        boolean isParticipating = playgroundMemberRepository
                .existsByPlaygroundIdAndMemberId(response.id(), member.getId());

        // then
        assertThat(isParticipating).isTrue();
    }

    @DisplayName("이미 참여한 놀이터가 있을 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenAlreadyParticipateOther() {
        // given
        Playground playground = savePlayground();
        Member member = saveMember("김도선");
        playgroundMemberRepository.save(new PlaygroundMember(playground, member));

        SavePlaygroundRequest request = new SavePlaygroundRequest(37.5173316, 127.1011661);

        // when, then
        assertThatThrownBy(() -> playgroundCommandService.save(request, member.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여한 놀이터가 존재합니다.");

    }

    @DisplayName("놀이터 생성시, 놀이터 범위에 다른 겹치는 놀이터 범위가 존재하면 예외가 발생한다. : 경도차이")
    @Test
    void throwExceptionWhenOverlapPlaygroundScopeWithLongitudeDiff() {
        // given
        Member member = saveMember("김도선");
        double latitude = 37.516382;
        double longitudeA = 127.120040;
        double longitudeFar299mFromA = GeoCalculator.calculateLongitudeOffset(latitude, longitudeA, 299);
        savePlayground(latitude, longitudeA);
        SavePlaygroundRequest request = new SavePlaygroundRequest(latitude, longitudeFar299mFromA);

        // when, then
        assertThatThrownBy(() -> playgroundCommandService.save(request, member.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("생성할 놀이터 범위내에 겹치는 다른 놀이터 범위가 있습니다.");
    }

    @DisplayName("놀이터 생성시, 놀이터 범위에 다른 겹치는 놀이터 범위가 존재하면 예외가 발생한다. : 위도차이")
    @Test
    void throwExceptionWhenOverlapPlaygroundScopeWithLatitudeDiff() {
        // given
        Member member = saveMember("김도선");
        double latitudeA = 37.516382;
        double longitude = 127.120040;
        double latitude299mFromA = GeoCalculator.calculateLatitudeOffset(latitudeA, 299);
        savePlayground(latitudeA, longitude);
        SavePlaygroundRequest request = new SavePlaygroundRequest(latitude299mFromA, longitude);

        // when, then
        assertThatThrownBy(() -> playgroundCommandService.save(request, member.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("생성할 놀이터 범위내에 겹치는 다른 놀이터 범위가 있습니다.");
    }

    @DisplayName("놀이터 참여시, 이미 참여한 놀이터가 존재하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenAlreadyJoinPlayground() {
        // given
        Member member = saveMember("김도선");
        double latitude = 37.516382;
        double longitude = 127.120040;
        Playground playground = savePlayground(latitude, longitude);
        Playground secondPlayground = savePlayground(GeoCalculator.calculateLatitudeOffset(latitude, 500), longitude);
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member
                )
        );

        // when, then
        assertThatThrownBy(() -> playgroundCommandService.joinPlayground(member.getId(), secondPlayground.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여한 놀이터가 존재합니다.");
    }

    @DisplayName("놀이터를 나갈 수 있다")
    @Test
    void leavePlayground() {
        // given
        Member member = saveMember("김도선");
        Playground playground = savePlayground();
        PlaygroundMember playgroundMember = playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member
                )
        );

        // when
        playgroundCommandService.leavePlayground(member.getId());
        boolean isPlaygroundMemberPresent = playgroundMemberRepository.findById(playgroundMember.getId()).isPresent();

        // then
        assertThat(isPlaygroundMemberPresent).isFalse();
    }

    @DisplayName("놀이터를 나갔을 때, 놀이터에 참여중인 멤버가 하나도 없으면 놀이터는 삭제된다.")
    @Test
    void deletePlaygroundWhenLeavePlaygroundAndNoMemberInPlayground() {
        // given
        Member member = saveMember("김도선");
        Playground playground = savePlayground();
        PlaygroundMember playgroundMember = playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member
                )
        );

        // when
        playgroundCommandService.leavePlayground(member.getId());
        boolean isExistPlayground = playgroundRepository.existsById(playground.getId());

        // then
        assertThat(isExistPlayground).isFalse();
    }
}
