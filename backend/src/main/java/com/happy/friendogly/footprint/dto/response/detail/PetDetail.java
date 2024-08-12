package com.happy.friendogly.footprint.dto.response.detail;

import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;

public record PetDetail(
        String name,
        String description,
        LocalDate birthDate,
        SizeType sizeType,
        Gender gender,
        String imageUrl
) {
    public PetDetail(Pet pet) {
        this(
                pet.getName().getValue(),
                pet.getDescription().getValue(),
                pet.getBirthDate().getValue(),
                pet.getSizeType(),
                pet.getGender(),
                pet.getImageUrl()
        );
    }
}
