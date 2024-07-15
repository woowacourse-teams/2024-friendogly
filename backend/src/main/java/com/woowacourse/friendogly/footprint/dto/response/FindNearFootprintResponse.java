package com.woowacourse.friendogly.footprint.dto.response;

import com.woowacourse.friendogly.footprint.domain.Footprint;

public record FindNearFootprintResponse(Long memberId, double latitude, double longitude) {

    public static FindNearFootprintResponse from(Footprint footprint) {
        return new FindNearFootprintResponse(
            footprint.getMember().getId(),
            footprint.getLocation().getLatitude(),
            footprint.getLocation().getLongitude()
        );
    }
}
