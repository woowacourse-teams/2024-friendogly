package com.woowacourse.friendogly.pet.dto.response;

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

}
