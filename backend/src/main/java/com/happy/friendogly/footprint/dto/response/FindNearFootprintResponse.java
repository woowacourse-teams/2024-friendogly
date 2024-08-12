package com.happy.friendogly.footprint.dto.response;

import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.WalkStatus;
import java.time.LocalDateTime;

public record FindNearFootprintResponse(
        Long footprintId,
        double latitude,
        double longitude,
        WalkStatus walkStatus,
        LocalDateTime createdAt,
        boolean isMine
) {

    public FindNearFootprintResponse(Footprint footprint, boolean isMine) {
        this(
                footprint.getId(),
                footprint.getLocation().getLatitude(),
                footprint.getLocation().getLongitude(),
                footprint.getWalkStatus(),
                footprint.getCreatedAt(),
                isMine
        );
    }
}
