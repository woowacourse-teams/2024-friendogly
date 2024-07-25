package com.woowacourse.friendogly.footprint.dto.response;

import java.time.LocalDateTime;

public record SaveFootprintResponse(
        Long id,
        double latitude,
        double longitude,
        LocalDateTime createdAt
) {

}
