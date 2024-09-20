package com.happy.friendogly.chat.dto.request;

import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;

public record FindMessagesByTimeRangeRequest(
        @Past(message = "채팅 메시지 조회 since 시간은 과거 시간만 입력 가능합니다.")
        LocalDateTime since,

        @Past(message = "채팅 메시지 조회 until 시간은 과거 시간만 입력 가능합니다.")
        LocalDateTime until
) {

}
