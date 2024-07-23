package com.woowacourse.friendogly.footprint.dto.response;

import java.time.LocalDateTime;

public record FindMyLatestFootprintTimeResponse(
        LocalDateTime createdAt
) {

}
