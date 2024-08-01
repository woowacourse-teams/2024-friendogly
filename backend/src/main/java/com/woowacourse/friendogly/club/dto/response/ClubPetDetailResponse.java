package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.pet.domain.Pet;

public record ClubPetDetailResponse(
        Long id,
        String name,
        String imageUrl,
        boolean isMine
){

    public ClubPetDetailResponse(Pet pet, boolean isMine) {
        this(pet.getId(),pet.getName().getValue(), pet.getImageUrl(), isMine);
    }
}
