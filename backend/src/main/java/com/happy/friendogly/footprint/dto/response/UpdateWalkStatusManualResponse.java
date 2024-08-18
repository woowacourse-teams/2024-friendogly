package com.happy.friendogly.footprint.dto.response;

import com.happy.friendogly.footprint.domain.WalkStatus;
import java.time.LocalDateTime;

public record UpdateWalkStatusManualResponse(
        WalkStatus walkStatus,
        LocalDateTime changedWalkStatusTime
) {
}
