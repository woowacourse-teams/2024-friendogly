package com.woowacourse.friendogly.club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record FindSearchingClubRequest(
        @NotBlank(message = "필터링 조건은 필수입니다.")
        String filterCondition,

        @NotBlank(message = "도/광역시/특별시 정보는 필수 값입니다.")
        String province,

        @NotBlank(message = "시/군/구 정보는 필수 값입니다.")
        String city,

        @NotBlank(message = "읍/면/동 정보는 필수 값입니다.")
        String village,

        @NotEmpty(message = "반려견 성별 검색 조건은 필수입니다.")
        Set<String> genderParams,

        @NotEmpty(message = "반려견 크기 검색 조건은 필수입니다.")
        Set<String> sizeParams
) {

}
