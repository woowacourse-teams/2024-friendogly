package com.woowacourse.friendogly.footprint.service;

import static com.woowacourse.friendogly.footprint.domain.WalkStatus.BEFORE;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
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

    public FootprintCommandService(
            FootprintRepository footprintRepository,
            MemberRepository memberRepository,
            PetRepository petRepository
    ) {
        this.footprintRepository = footprintRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
    }

    public SaveFootprintResponse save(Long memberId, SaveFootprintRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 사용자 ID입니다."));

        validatePetExistence(member);
        validateRecentFootprintExists(memberId);

        Footprint footprint = footprintRepository.save(
                Footprint.builder()
                        .member(member)
                        .location(new Location(request.latitude(), request.longitude()))
                        .walkStatus(BEFORE)
                        .build()
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
}
