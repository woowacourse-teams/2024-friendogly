package com.woowacourse.friendogly.pet.dto.response;

import com.woowacourse.friendogly.pet.domain.Pet;

public record SavePetResponse(
        Long id,
        Long memberId,
        String name,
        String description,
        String sizeType,
        String gender,
        boolean isNeutered,
        String imageUrl
) {

    public SavePetResponse(Pet pet) {
        this(
                pet.getId(),
                pet.getMember().getId(),
                pet.getName(),
                pet.getDescription(),
                pet.getSizeType().toString(),
                pet.getGender().toString(),
                pet.isNeutered(),
                pet.getImageUrl()
        );
    }
}
