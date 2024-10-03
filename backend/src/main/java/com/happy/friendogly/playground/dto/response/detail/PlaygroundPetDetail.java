package com.happy.friendogly.playground.dto.response.detail;

import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.time.LocalDate;
import java.util.List;

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

    public static List<PlaygroundPetDetail> getListOf(
            List<Pet> pets,
            PlaygroundMember petOwner,
            boolean isMyPet
    ) {
        return pets.stream()
                .map(pet -> PlaygroundPetDetail.of(
                        petOwner.getId(),
                        pet,
                        petOwner.getMessage(),
                        petOwner.isInside(),
                        isMyPet
                ))
                .toList();
    }

    private static PlaygroundPetDetail of(
            Long memberId,
            Pet pet,
            String message,
            boolean isArrival,
            boolean isMine
    ) {
        return new PlaygroundPetDetail(
                memberId,
                pet.getId(),
                pet.getName().getValue(),
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
