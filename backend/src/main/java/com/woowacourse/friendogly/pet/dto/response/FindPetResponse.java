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
        String image
) {
    public static FindPetResponse from(Pet pet){
        return new FindPetResponse(
                pet.getName(),
                pet.getDescription(),
                pet.getBirthDate(),
                pet.getSizeType().getValue(),
                pet.getGender().getValue(),
                pet.isNeutered(),
                pet.getImage()
                );
    }
}
