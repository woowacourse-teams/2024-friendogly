package com.happy.friendogly.playground.dto.response.detail;

import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;

public record PlaygroundPetDetail(
        Long memberId,
        Long petId,
        String name,
        LocalDate birthDate,
        SizeType sizeType,
        Gender gender,
        String imageUrl,
        String message,
        boolean isArrival,
        boolean isMine
) {

    public static PlaygroundPetDetail dummyOf(
            Long memberId,
            Pet pet,
            String message,
            boolean isArrival,
            boolean isMine
    ) {
        return new PlaygroundPetDetail(
                memberId,
                pet.getId(),
                pet.getName().toString(),
                pet.getBirthDate().getValue(),
                pet.getSizeType(),
                pet.getGender(),
                pet.getImageUrl(),
                message,
                isArrival,
                isMine
        );
    }
}
