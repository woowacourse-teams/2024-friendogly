package com.woowacourse.friendogly.pet.dto.response;

import com.woowacourse.friendogly.pet.domain.Pet;
import java.time.LocalDate;

public record FindPetResponse(
        String name,
        String description,
        LocalDate birthDate,
        String sizeType,
        String gender,
        boolean isNeutered,
        String imageUrl
) {

    public FindPetResponse(Pet pet) {
        this(
                pet.getName(),
                pet.getDescription(),
                pet.getBirthDate(),
                pet.getSizeType().toString(),
                pet.getGender().toString(),
                pet.isNeutered(),
                pet.getImageUrl()
        );
    }
}
