package com.happy.friendogly.footprint.dto.response;

import java.time.LocalDateTime;

public record FindMyLatestFootprintTimeAndPetExistenceResponse(
        LocalDateTime createdAt,
        boolean hasPet
) {

}
