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
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaygroundCommandService {

    private static final int PLAYGROUND_RADIUS = 150;

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

        return new SavePlaygroundResponse(savedPlayground);
    }

    private void validateExistParticipatingPlayground(Member member) {
        if (playgroundMemberRepository.existsByMemberId(member.getId())) {
            throw new FriendoglyException("이미 참여한 놀이터가 존재합니다.");
        }
    }

    private void validateOverlapPlayground(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        Location startLatitudeLocation = location.findLocationWithLatitudeDiff(-(PLAYGROUND_RADIUS * 2));
        Location endLatitudeLocation = location.findLocationWithLatitudeDiff(PLAYGROUND_RADIUS * 2);
        Location startLongitudeLocation = location.findLocationWithLongitudeDiff(-(PLAYGROUND_RADIUS * 2));
        Location endLongitudeLocation = location.findLocationWithLongitudeDiff(PLAYGROUND_RADIUS * 2);

        List<Playground> playgrounds = playgroundRepository.findAllByLatitudeBetweenAndLongitudeBetween(
                startLatitudeLocation.getLatitude(),
                endLatitudeLocation.getLatitude(),
                startLongitudeLocation.getLongitude(),
                endLongitudeLocation.getLongitude()
        );

        boolean isExistWithinRadius = playgrounds.stream()
                .anyMatch(playground -> location.isWithin(playground.getLocation(),
                        PLAYGROUND_RADIUS));

        if (isExistWithinRadius) {
            throw new FriendoglyException("생성할 놀이터 범위내에 겹치는 다른 놀이터 범위가 있습니다.");
        }
    }
}
