package com.woowacourse.friendogly.pet.dto.request;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public record SavePetRequest(

        @NotBlank(message = "name은 빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 15, message = "이름은 1글자 이상 15글자 이하여야 합니다.")
        String name,

        @NotBlank(message = "description은 빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 15, message = "설명은 1글자 이상 15글자 이하여야 합니다.")
        String description,

        @NotNull(message = "birthDate는 빈 문자열이나 null을 입력할 수 없습니다.")
        @PastOrPresent(message = "birthDate는 현재 날짜와 같거나 이전이어야 합니다.")
        LocalDate birthDate,

        @NotBlank(message = "sizeType은 빈 문자열이나 null을 입력할 수 없습니다.")
        String sizeType,

        @NotBlank(message = "gender는 빈 문자열이나 null을 입력할 수 없습니다.")
        String gender,

        @URL
        String imageUrl
) {

    public Pet toEntity(Member member) {
        return Pet.builder()
                .member(member)
                .name(name)
                .description(description)
                .birthDate(birthDate)
                .sizeType(SizeType.toSizeType(sizeType))
                .gender(Gender.toGender(gender))
                .imageUrl(imageUrl)
                .build();
    }
}
