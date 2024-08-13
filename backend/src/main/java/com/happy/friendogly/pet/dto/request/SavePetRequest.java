package com.happy.friendogly.pet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SavePetRequest(
        @NotBlank(message = "name은 빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 8, message = "이름은 1글자 이상 8글자 이하여야 합니다.")
        String name,

        @NotBlank(message = "description은 빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 20, message = "설명은 1글자 이상 20글자 이하여야 합니다.")
        String description,

        @NotNull(message = "birthDate는 빈 문자열이나 null을 입력할 수 없습니다.")
        @PastOrPresent(message = "birthDate는 현재 날짜와 같거나 이전이어야 합니다.")
        LocalDate birthDate,

        @NotBlank(message = "sizeType은 빈 문자열이나 null을 입력할 수 없습니다.")
        String sizeType,

        @NotBlank(message = "gender는 빈 문자열이나 null을 입력할 수 없습니다.")
        String gender
) {

}
