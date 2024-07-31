package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.pet.domain.Pet;

public record ClubPetDetailResponse(
        Long id,
        String name,
        String imageUrl
){

    public ClubPetDetailResponse(Pet pet) {
        this(pet.getId(),pet.getName().getValue(), pet.getImageUrl());
    }
}
