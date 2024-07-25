package com.woowacourse.friendogly.club.dto.request;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public record SaveClubRequest(
        @NotBlank(message = "제목을 작성해주세요.")
        @Size(min = 1, max = 100, message = "제목은 1글자 100글자 사이입니다.")
        String title,

        @NotBlank(message = "본문을 작성해주세요.")
        @Size(min = 1, max = 1000, message = "본문은 1글자 1000글자 사이입니다.")
        String content,

        @NotBlank(message = "모임 사진을 등록해주세요.")
        String imageUrl,

        @NotBlank(message = "주소를 읽을 수 없습니다. 다시 시도해주세요.")
        String address,

        @NotEmpty(message = "모임에 참여가능한 성별을 선택해주세요.")
        Set<Gender> allowedGenders,

        @NotEmpty(message = "모임에 참여가능한 댕댕이 사이즈를 선택해주세요.")
        Set<SizeType> allowedSizes,

        @Min(value = 1, message = "모임 최대 인원은 1명 이상입니다.")
        @Max(value = 5, message = "모임 최대 인원은 5명 이하입니다.")
        int memberCapacity,

        @NotEmpty(message = "모임에 참여할 댕댕이를 선택해주세요.")
        List<Long> participatingPetsId
) {

    public Club toEntity(Member member) {
        return Club.create(
                title,
                content,
                address,
                memberCapacity,
                member,
                allowedGenders,
                allowedSizes,
                imageUrl
        );
    }
}
