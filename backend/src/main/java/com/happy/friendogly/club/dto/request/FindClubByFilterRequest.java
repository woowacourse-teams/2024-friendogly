package com.happy.friendogly.club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

public record FindClubByFilterRequest(
        @NotBlank(message = "필터링 조건은 필수입니다.")
        String filterCondition,

        @NotBlank(message = "도/광역시/특별시 정보는 필수 값입니다.")
        String province,

        String city,

        String village,

        @NotEmpty(message = "반려견 성별 검색 조건은 필수입니다.")
        Set<String> genderParams,

        @NotEmpty(message = "반려견 크기 검색 조건은 필수입니다.")
        Set<String> sizeParams,

        /**
         * TODO: 일단 nullable 하게 변경.
         *       안드로이드 페이징 코드 머지됐을 때 non-nullable하게 바꾸기 (일부러 타입도 wrapper 타입으로 변경해둠)
         */
//        @Positive(message = "페이지 사이즈는 1 이상의 정수 입니다.")
        Integer pageSize,

//        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastFoundCreatedAt,

//        @PositiveOrZero(message = "마지막으로 조회한 모임 ID는 0 이상의 정수 입니다.")
        Long lastFoundId
) {

}
