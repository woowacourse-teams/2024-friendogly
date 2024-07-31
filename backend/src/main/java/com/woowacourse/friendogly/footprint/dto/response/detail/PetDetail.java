package com.woowacourse.friendogly.footprint.dto.response.detail;

import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;

public record PetDetail(
        String name,
        String description,
        LocalDate birthDate,
        SizeType sizeType,
        Gender gender,
        String imageUrl
) {
    public static PetDetail from(Pet pet) {
        return new PetDetail(
                pet.getName().getValue(),
                pet.getDescription().getValue(),
                pet.getBirthDate().getValue(),
                pet.getSizeType(),
                pet.getGender(),
                pet.getImageUrl()
        );
    }
}
