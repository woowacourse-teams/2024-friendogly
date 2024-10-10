package com.happy.friendogly.playground.dto.response;

import com.happy.friendogly.playground.domain.Playground;

public record FindPlaygroundLocationResponse(

        Long id,
        double latitude,
        double longitude,
        boolean isParticipating
) {

    public FindPlaygroundLocationResponse(Playground playground, boolean isParticipating) {
        this(
                playground.getId(),
                playground.getLocation().getLatitude(),
                playground.getLocation().getLongitude(),
                isParticipating
        );
    }
}
