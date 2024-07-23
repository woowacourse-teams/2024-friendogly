package com.woowacourse.friendogly.footprint.dto.response;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import java.time.LocalDateTime;

public record FindNearFootprintResponse(
        Long footprintId,
        double latitude,
        double longitude,
        LocalDateTime createdAt,
        boolean isMine,
        String imageUrl
) {

    public FindNearFootprintResponse(Footprint footprint, boolean isMine) {
        this(
                footprint.getId(),
                footprint.getLocation().getLatitude(),
                footprint.getLocation().getLongitude(),
                footprint.getCreatedAt(),
                isMine,
                footprint.getImageUrl()
        );
    }
}
