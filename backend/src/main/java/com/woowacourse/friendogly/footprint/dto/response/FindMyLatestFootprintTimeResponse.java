package com.woowacourse.friendogly.footprint.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record FindMyLatestFootprintTimeResponse(
        // TODO: 패턴을 통일시키고 나서 어노테이션 지우기
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt
) {

}
