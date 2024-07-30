package com.woowacourse.friendogly.footprint.dto.response.detail;

import com.woowacourse.friendogly.pet.domain.Pet;
import java.time.LocalDate;

public record PetDetail(
            String name,
            String description,
            LocalDate birthDate,
            String sizeType,
            String gender,
            String imageUrl) {
        public static PetDetail from(Pet pet) {
            return new PetDetail(
                    pet.getName().getValue(),
                    pet.getDescription().getValue(),
                    pet.getBirthDate().getValue(),
                    pet.getSizeType().toString(),
                    pet.getGender().toString(),
                    pet.getImageUrl());
        }
    }
