package com.happy.friendogly.club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

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

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastFoundCreatedAt,

        @PositiveOrZero(message = "마지막으로 조회한 모임 ID는 0 이상의 정수 입니다.")
        Long lastFoundId
) {

}
