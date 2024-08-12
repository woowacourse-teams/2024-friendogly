package com.happy.friendogly.footprint.dto.response;

import com.happy.friendogly.footprint.domain.WalkStatus;
import com.happy.friendogly.footprint.dto.response.detail.PetDetail;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
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
