package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FootprintQueryService {

    private static final int FOOTPRINT_DURATION_HOURS = 24;

    private final FootprintRepository footprintRepository;
    private final PetRepository petRepository;

    public FootprintQueryService(
            FootprintRepository footprintRepository,
            PetRepository petRepository
    ) {
        this.footprintRepository = footprintRepository;
        this.petRepository = petRepository;
    }

    public FindOneFootprintResponse findOne(Long memberId, Long footprintId) {
        Footprint footprint = footprintRepository.findById(footprintId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Footprint ID입니다."));

        Member member = footprint.getMember();
        List<Pet> pets = petRepository.findByMemberId(member.getId());
        boolean isMine = footprint.isCreatedBy(memberId);
        LocalDateTime changedWalkStatusTime = footprint.findChangedWalkStatusTime();
        return FindOneFootprintResponse.of(
                member,
                pets,
                footprint.getWalkStatus(),
                changedWalkStatusTime,
                isMine
        );
    }

    public List<FindNearFootprintResponse> findNear(Long memberId, FindNearFootprintRequest request) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(FOOTPRINT_DURATION_HOURS);
        List<Footprint> recentFootprints = footprintRepository.findByCreatedAtAfter(startTime);

        Location currentLocation = new Location(request.latitude(), request.longitude());

        return recentFootprints.stream()
                .filter(footprint -> footprint.isNear(currentLocation))
                .filter(footprint -> !footprint.isDeleted())
                .map(footprint -> new FindNearFootprintResponse(footprint, footprint.isCreatedBy(memberId)))
                .toList();
    }

    public FindMyLatestFootprintTimeAndPetExistenceResponse findMyLatestFootprintTimeAndPetExistence(Long memberId) {
        LocalDateTime createdAt = footprintRepository.findTopOneByMemberIdOrderByCreatedAtDesc(memberId)
                .map(Footprint::getCreatedAt)
                .orElse(null);
        boolean hasPet = petRepository.existsByMemberId(memberId);

        return new FindMyLatestFootprintTimeAndPetExistenceResponse(createdAt, hasPet);
    }
}
