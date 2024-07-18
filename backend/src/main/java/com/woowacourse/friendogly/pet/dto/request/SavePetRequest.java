package com.woowacourse.friendogly.pet.dto.request;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;

public record SavePetRequest(
        Long memberId,
        String name,
        String description,
        LocalDate birthDate,
        String sizeType,
        String gender,
        boolean isNeutered,
        String imageUrl
) {
    public Pet toEntity(Member member) {
        return Pet.builder()
                .member(member)
                .name(name)
                .description(description)
                .birthDate(birthDate)
                .sizeType(SizeType.toSizeType(sizeType))
                .gender(Gender.toGender(gender))
                .isNeutered(isNeutered)
                .imageUrl(imageUrl)
                .build();
    }
}
