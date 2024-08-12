package com.happy.friendogly.footprint.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.Location;
import com.happy.friendogly.footprint.domain.WalkStatus;
import com.happy.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.happy.friendogly.footprint.dto.request.UpdateWalkStatusRequest;
import com.happy.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.happy.friendogly.footprint.dto.response.UpdateWalkStatusResponse;
import com.happy.friendogly.footprint.repository.FootprintRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import com.happy.friendogly.notification.service.FcmNotificationService;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FootprintCommandService {

    private static final int FOOTPRINT_COOLDOWN_SECOND = 30;

    private final FootprintRepository footprintRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final FcmNotificationService fcmNotificationService;
    private final DeviceTokenRepository deviceTokenRepository;

    public FootprintCommandService(
            FootprintRepository footprintRepository,
            MemberRepository memberRepository,
            PetRepository petRepository,
            FcmNotificationService fcmNotificationService,
            DeviceTokenRepository deviceTokenRepository) {
        this.footprintRepository = footprintRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
        this.fcmNotificationService = fcmNotificationService;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public SaveFootprintResponse save(Long memberId, SaveFootprintRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 사용자 ID입니다."));

        validatePetExistence(member);
        validateRecentFootprintExists(memberId);

        footprintRepository.findTopOneByMemberIdOrderByCreatedAtDesc(memberId)
                .ifPresent(Footprint::updateToDeleted);

        Footprint footprint = footprintRepository.save(
                Footprint.builder()
                        .member(member)
                        .location(new Location(request.latitude(), request.longitude()))
                        .build()
        );

        List<String> nearDeviceTokens = findNearDeviceTokens(footprint);
        String memberName = member.getName().getValue();

        fcmNotificationService.sendNotification(
                "반갑개",
                "내 산책 장소에 " + memberName + "님도 산책온대요!",
                nearDeviceTokens
        );

        return new SaveFootprintResponse(
                footprint.getId(),
                footprint.getLocation().getLatitude(),
                footprint.getLocation().getLongitude(),
                footprint.getCreatedAt()
        );
    }

    private void validatePetExistence(Member member) {
        List<Pet> pets = petRepository.findByMemberId(member.getId());
        if (pets.isEmpty()) {
            throw new FriendoglyException("펫을 등록해야만 발자국을 생성할 수 있습니다.");
        }
    }

    private void validateRecentFootprintExists(Long memberId) {
        boolean exists = footprintRepository.existsByMemberIdAndCreatedAtAfter(
                memberId,
                LocalDateTime.now().minusSeconds(FOOTPRINT_COOLDOWN_SECOND)
        );

        if (exists) {
            throw new FriendoglyException(String.format("마지막 발자국을 찍은 뒤 %d초가 경과되지 않았습니다.", FOOTPRINT_COOLDOWN_SECOND));
        }
    }

    public UpdateWalkStatusResponse updateWalkStatus(Long memberId, UpdateWalkStatusRequest request) {
        Footprint footprint = footprintRepository.getTopOneByMemberIdOrderByCreatedAtDesc(memberId);
        if (footprint.isDeleted()) {
            throw new FriendoglyException("가장 최근 발자국이 삭제된 상태입니다.");
        }

        WalkStatus beforeWalkStatus = footprint.getWalkStatus();


        if(beforeWalkStatus.isBefore() && footprint.getWalkStatus().isOngoing()){
            List<String> nearDeviceTokens = findNearDeviceTokens(footprint);
            String memberName = footprint.getMember().getName().getValue();

            fcmNotificationService.sendNotification("반갑개",
                    "내 산책장소에 "+memberName+"님이 산책을 시작했어요!",
                    nearDeviceTokens
            );
        }



        footprint.updateWalkStatusWithCurrentLocation(new Location(request.latitude(), request.longitude()));
        return new UpdateWalkStatusResponse(footprint.getWalkStatus());
    }

    private List<String> findNearDeviceTokens(Footprint standardFootprint) {
        List<Footprint> footprints = footprintRepository.findByIsDeletedFalse();

        return footprints.stream()
                .filter(otherFootprint -> otherFootprint.isInsideBoundary(standardFootprint.getLocation()))
                .map(otherFootprint -> otherFootprint.getMember().getId())
                .map(otherMemberId -> deviceTokenRepository.findByMemberId(otherMemberId).get().getDeviceToken())
                .toList();
    }
}
