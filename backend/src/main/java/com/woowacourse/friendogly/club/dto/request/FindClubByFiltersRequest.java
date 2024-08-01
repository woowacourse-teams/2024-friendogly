package com.woowacourse.friendogly.club.dto.request;

import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record FindClubByFiltersRequest(
        @NotBlank(message = "필터링 조건은 필수입니다.")
        String filterCondition,

        @NotBlank(message = "주소 정보는 필수입니다.")
        String address,

        @NotEmpty(message = "반려견 성별 검색 조건은 필수입니다.")
        Set<Gender> genderParams,

        @NotEmpty(message = "반려견 크기 검색 조건은 필수입니다.")
        Set<SizeType> sizeParams
) {

}
