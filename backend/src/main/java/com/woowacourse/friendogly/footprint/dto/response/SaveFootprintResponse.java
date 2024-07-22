package com.woowacourse.friendogly.footprint.dto.response;

public record SaveFootprintResponse(
        Long id,
        double latitude,
        double longitude
) {

}
