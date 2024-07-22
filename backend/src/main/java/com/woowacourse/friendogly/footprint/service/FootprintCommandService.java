package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.UpdateFootprintImageRequest;
import com.woowacourse.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.UpdateFootprintImageResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
                        .build()
        );

        return new SaveFootprintResponse(
                footprint.getId(),
                footprint.getLocation().getLatitude(),
                footprint.getLocation().getLongitude()
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

    public UpdateFootprintImageResponse updateFootprintImage(Long footprintId, UpdateFootprintImageRequest request) {
        Footprint footprint = footprintRepository.findById(footprintId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Footprint ID입니다."));

        // TODO: 자신의 발자국이 아니면 사진을 업데이트할 수 없다. 검증 추가하기!
        MultipartFile multipartFile = request.imageFile();
        // TODO: 더미 데이터 URL입니다. 나중에 이미지 저장소(AWS S3)와 연동 필요 !!!
        // TODO: 연동 완료되면 테스트도 작성 필요합니다.
        String imageUrl = "https://img.extmovie.com/files/attach/images/148/921/189/074/6a71e831234e113e3ed215405098109c.jpg";
        footprint.updateImageUrl(imageUrl);
        return new UpdateFootprintImageResponse(imageUrl);
    }
}
