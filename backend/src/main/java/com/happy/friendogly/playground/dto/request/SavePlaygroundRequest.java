package com.happy.friendogly.playground.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record SavePlaygroundRequest(

        @NotNull
        @DecimalMin(value = "-90.0", message = "위도는 -90도 이상 90도 이하로 입력해 주세요.")
        @DecimalMax(value = "90.0", message = "위도는 -90도 이상 90도 이하로 입력해 주세요.")
        double latitude,

        @NotNull
        @DecimalMin(value = "-180.0", message = "경도는 -180도 이상 180도 이하로 입력해 주세요.")
        @DecimalMax(value = "180.0", message = "경도는 -180도 이상 180도 이하로 입력해 주세요.")
        double longitude
) {
}
