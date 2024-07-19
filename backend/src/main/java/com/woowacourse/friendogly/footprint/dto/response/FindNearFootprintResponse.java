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
    boolean isMine
) {

    public static FindNearFootprintResponse from(Footprint footprint, Long memberId) {
        return new FindNearFootprintResponse(
            footprint.getId(),
            footprint.getLocation().getLatitude(),
            footprint.getLocation().getLongitude(),
            footprint.getCreatedAt(),
            memberId.equals(footprint.getMember().getId())
        );
    }
}
