package com.woowacourse.friendogly.footprint.dto.response;

import com.woowacourse.friendogly.footprint.domain.WalkStatus;
import com.woowacourse.friendogly.footprint.dto.response.detail.PetDetail;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import java.time.LocalDateTime;
import java.util.List;

public record FindOneFootprintResponse(
        Long memberId,
        String memberName,
        WalkStatus walkStatus,
        LocalDateTime changedWalkStatusTime,
        List<PetDetail> pets,
        boolean isMine
) {

    public static FindOneFootprintResponse of(
            Member member,
            List<Pet> pets,
            WalkStatus walkStatus,
            LocalDateTime changedWalkStatusTime,
            boolean isMine
    ) {
        List<PetDetail> petDetails = pets.stream()
                .map(PetDetail::new)
                .toList();
        return new FindOneFootprintResponse(
                member.getId(),
                member.getName().getValue(),
                walkStatus,
                changedWalkStatusTime,
                petDetails,
                isMine);
    }
}
