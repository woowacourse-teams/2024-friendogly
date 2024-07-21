package com.woowacourse.friendogly.footprint.dto.response;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;

public record FindOneFootprintResponse(
    String memberName,
    String petName,
    String petDescription,
    LocalDate petBirthDate,
    SizeType petSizeType,
    Gender petGender,
    String footprintImageUrl
) {

    public FindOneFootprintResponse(Member member, Footprint footprint) {
        this(
            member.getName().getValue(),
            null,
            null,
            null,
            null,
            null,
            footprint.getImageUrl()
        );
    }

    public FindOneFootprintResponse(Member member, Pet mainPet, Footprint footprint) {
        this(
            member.getName().getValue(),
            mainPet.getName().getValue(),
            mainPet.getDescription().getValue(),
            mainPet.getBirthDate().getValue(),
            mainPet.getSizeType(),
            mainPet.getGender(),
            footprint.getImageUrl()
        );
    }
}
