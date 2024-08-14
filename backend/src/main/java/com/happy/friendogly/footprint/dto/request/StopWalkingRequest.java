package com.happy.friendogly.footprint.dto.request;

import jakarta.validation.constraints.NotNull;

public record StopWalkingRequest(
        @NotNull
        Long footprintId
) {

}
