package com.woowacourse.friendogly.pet.dto.response;

import com.woowacourse.friendogly.pet.domain.Pet;
import java.time.LocalDate;

public record FindPetResponse(
        Long id,
        Long memberId,
        String name,
        String description,
        LocalDate birthDate,
        String sizeType,
        String gender,
        String imageUrl
) {

    public FindPetResponse(Pet pet) {
        this(
                pet.getId(),
                pet.getMember().getId(),
                pet.getName().getValue(),
                pet.getDescription().getValue(),
                pet.getBirthDate().getValue(),
                pet.getSizeType().toString(),
                pet.getGender().toString(),
                pet.getImageUrl()
        );
    }
}
