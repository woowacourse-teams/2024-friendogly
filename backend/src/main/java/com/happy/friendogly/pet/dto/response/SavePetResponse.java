package com.happy.friendogly.pet.dto.response;

import com.happy.friendogly.pet.domain.Pet;
import java.time.LocalDate;

public record SavePetResponse(
        Long id,
        Long memberId,
        String name,
        String description,
        LocalDate birthDate,
        String sizeType,
        String gender,
        String imageUrl
) {

    public SavePetResponse(Pet pet) {
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
