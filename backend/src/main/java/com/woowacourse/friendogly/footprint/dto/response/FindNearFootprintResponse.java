package com.woowacourse.friendogly.footprint.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import java.time.LocalDateTime;

public record FindNearFootprintResponse(
    Long footprintId,
    double latitude,
    double longitude,
    // TODO: 패턴을 통일시키고 나서 어노테이션 지우기
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
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
