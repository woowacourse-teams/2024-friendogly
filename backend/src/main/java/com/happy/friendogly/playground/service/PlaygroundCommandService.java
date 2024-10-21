package com.happy.friendogly.playground.service;

import static com.happy.friendogly.common.ErrorCode.OVERLAP_PLAYGROUND_CREATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.service.NotificationService;
import com.happy.friendogly.notification.service.PlaygroundNotificationService;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundArrivalRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundMemberMessageRequest;
import com.happy.friendogly.playground.dto.response.SaveJoinPlaygroundMemberResponse;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundArrivalResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundMemberMessageResponse;
import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaygroundCommandService {

    public static final int OUTSIDE_MEMBER_KEEP_TIME = 2;
    public static final String EVERY_30_MINUTE = "0 0/30 * * * *";

    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundMemberRepository playgroundMemberRepository;
    private final MemberRepository memberRepository;
    private final PlaygroundNotificationService playgroundNotificationService;

    public PlaygroundCommandService(
            PlaygroundRepository playgroundRepository,
            PlaygroundMemberRepository playgroundMemberRepository,
            MemberRepository memberRepository, NotificationService notificationService,
            PlaygroundNotificationService playgroundNotificationService) {
        this.playgroundRepository = playgroundRepository;
        this.playgroundMemberRepository = playgroundMemberRepository;
        this.memberRepository = memberRepository;
        this.playgroundNotificationService = playgroundNotificationService;
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
            throw new FriendoglyException("이미 참여한 놀이터가 존재합니다.", ErrorCode.ALREADY_PARTICIPATE_PLAYGROUND, BAD_REQUEST);
        }
    }

    private void validateOverlapPlayground(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        Location startLatitudeLocation = location.minusLatitudeByOverlapDistance();
        Location endLatitudeLocation = location.plusLatitudeByOverlapDistance();
        Location startLongitudeLocation = location.minusLongitudeByOverlapDistance();
        Location endLongitudeLocation = location.plusLongitudeByOverlapDistance();

        List<Playground> playgrounds = playgroundRepository.findAllByLatitudeBetweenAndLongitudeBetween(
                startLatitudeLocation.getLatitude(),
                endLatitudeLocation.getLatitude(),
                startLongitudeLocation.getLongitude(),
                endLongitudeLocation.getLongitude()
        );

        boolean isExistWithinRadius = playgrounds.stream()
                .anyMatch(playground -> playground.isOverlapLocation(location));

        if (isExistWithinRadius) {
            throw new FriendoglyException(
                    "생성할 놀이터 범위내에 겹치는 다른 놀이터 범위가 있습니다.",
                    OVERLAP_PLAYGROUND_CREATION,
                    BAD_REQUEST
            );
        }
    }

    public SaveJoinPlaygroundMemberResponse joinPlayground(Long memberId, Long playgroundId) {
        Playground playground = playgroundRepository.getById(playgroundId);
        Member member = memberRepository.getById(memberId);
        List<PlaygroundMember> existingPlaygroundMembers = playgroundMemberRepository
                .findAllByPlaygroundId(playgroundId);

        validateExistParticipatingPlayground(member);

        PlaygroundMember playgroundMember = playgroundMemberRepository.save(
                new PlaygroundMember(playground, member)
        );

        playgroundNotificationService.sendJoinNotification(member.getName().getValue(), existingPlaygroundMembers);

        return new SaveJoinPlaygroundMemberResponse(playgroundMember);
    }

    public void leavePlayground(Long memberId) {
        playgroundMemberRepository.findByMemberId(memberId)
                .ifPresent(playgroundMember -> {
                    playgroundMemberRepository.delete(playgroundMember);
                    deletePlaygroundConditional(playgroundMember.getPlayground());
                });
    }

    private void deletePlaygroundConditional(Playground playground) {
        if (!playgroundMemberRepository.existsByPlaygroundId(playground.getId())) {
            playgroundRepository.delete(playground);
        }
    }

    public UpdatePlaygroundArrivalResponse updateArrival(UpdatePlaygroundArrivalRequest request, Long memberId) {
        PlaygroundMember playgroundMember = playgroundMemberRepository.getByMemberId(memberId);
        Playground playground = playgroundMember.getPlayground();

        Location location = new Location(request.latitude(), request.longitude());

        boolean pastIsInsideBoundary = playgroundMember.isInside();
        boolean changedIsInsideBoundary = playground.isInsideBoundary(location);

        if (pastIsInsideBoundary && !changedIsInsideBoundary) {
            playgroundMember.updateExitTime(LocalDateTime.now());
        }

        playgroundMember.updateIsInside(changedIsInsideBoundary);

        return new UpdatePlaygroundArrivalResponse(changedIsInsideBoundary);
    }

    public UpdatePlaygroundMemberMessageResponse updateMemberMessage(
            UpdatePlaygroundMemberMessageRequest request,
            Long memberId
    ) {
        PlaygroundMember playgroundMember = playgroundMemberRepository.getByMemberId(memberId);
        playgroundMember.updateMessage(request.message());
        return new UpdatePlaygroundMemberMessageResponse(playgroundMember.getMessage());
    }

    @Scheduled(cron = EVERY_30_MINUTE)
    public void deleteJoinMemberIntervalTime() {
        LocalDateTime outsideMemberKeepTime = LocalDateTime.now().minusHours(OUTSIDE_MEMBER_KEEP_TIME);

        List<PlaygroundMember> deletePlaygroundMembers = playgroundMemberRepository.findAllByIsInside(false).stream()
                .filter(
                        p -> (p.getExitTime() == null && p.getParticipateTime().isBefore(outsideMemberKeepTime))
                                || (p.getExitTime() != null && p.getExitTime().isBefore(outsideMemberKeepTime))
                ).toList();

        playgroundMemberRepository.deleteAll(deletePlaygroundMembers);

        List<Long> deletePlaygroundCandidate = deletePlaygroundMembers.stream()
                .map(playgroundMember -> playgroundMember.getPlayground().getId())
                .toList();

        playgroundRepository.deleteAllHasNotMemberByIdIn(deletePlaygroundCandidate);

    }
}
