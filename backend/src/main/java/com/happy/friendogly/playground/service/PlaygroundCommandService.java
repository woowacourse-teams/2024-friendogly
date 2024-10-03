package com.happy.friendogly.playground.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import com.happy.friendogly.utils.GeoCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaygroundCommandService {

    public static final int PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD = 300;
    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundMemberRepository playgroundMemberRepository;
    private final MemberRepository memberRepository;

    public PlaygroundCommandService(
            PlaygroundRepository playgroundRepository,
            PlaygroundMemberRepository playgroundMemberRepository,
            MemberRepository memberRepository) {
        this.playgroundRepository = playgroundRepository;
        this.playgroundMemberRepository = playgroundMemberRepository;
        this.memberRepository = memberRepository;
    }

    public SavePlaygroundResponse save(SavePlaygroundRequest request, Long memberId) {
        Member member = memberRepository.getById(memberId);
        validateExistParticipatingPlayground(member);
        validateOverlapPlayground(request.latitude(), request.longitude());
        Playground savedPlayground = playgroundRepository.save(
                new Playground(new Location(request.latitude(), request.longitude()))
        );
        playgroundMemberRepository.save(new PlaygroundMember(savedPlayground, member));
        return SavePlaygroundResponse.from(savedPlayground);
    }

    private void validateExistParticipatingPlayground(Member member) {
        if (playgroundMemberRepository.existsByMemberId(member.getId())) {
            throw new FriendoglyException("이미 참여한 놀이터가 존재합니다.");
        }
    }

    private void validateOverlapPlayground(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        double startLatitude = GeoCalculator.calculateLatitudeOffset(latitude,
                -PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD);
        double endLatitude = GeoCalculator.calculateLatitudeOffset(latitude,
                PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD);
        double startLongitude = GeoCalculator.calculateLongitudeOffset(latitude, longitude,
                -PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD);
        double endLongitude = GeoCalculator.calculateLongitudeOffset(latitude, longitude,
                PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD);

        List<Playground> playgrounds = playgroundRepository.
                findAllByLatitudeBetweenAndLongitudeBetween(startLatitude, endLatitude, startLongitude, endLongitude);

        boolean isExistWithinRadius = playgrounds.stream()
                .anyMatch(playground -> location.isWithin(playground.getLocation(),
                        PLAYGROUND_EXCEPTION_DISTANCE_THRESHOLD));

        if (isExistWithinRadius) {
            throw new FriendoglyException("생성할 놀이터 범위내에 겹치는 다른 놀이터 범위가 있습니다.");
        }
    }
}
