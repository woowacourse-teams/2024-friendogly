package com.happy.friendogly.playground.dto.response;

import com.happy.friendogly.playground.domain.Playground;

public record SavePlaygroundResponse(

        Long id,
        double latitude,
        double longitude
) {

    public SavePlaygroundResponse(Playground playground) {
        this(
                playground.getId(),
                playground.getLocation().getLatitude(),
                playground.getLocation().getLongitude()
        );
    }
}
