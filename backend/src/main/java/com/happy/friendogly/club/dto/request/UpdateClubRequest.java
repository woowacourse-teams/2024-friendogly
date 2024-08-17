package com.happy.friendogly.club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateClubRequest(
        @NotBlank(message = "제목을 작성해주세요.")
        @Size(min = 1, max = 100, message = "제목은 1글자 100글자 사이입니다.")
        String title,

        @NotBlank(message = "본문을 작성해주세요.")
        @Size(min = 1, max = 1000, message = "본문은 1글자 1000글자 사이입니다.")
        String content,

        @NotBlank(message = "모임 상태를 추가해주세요.")
        String status
) {

}
