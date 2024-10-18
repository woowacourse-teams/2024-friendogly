package com.happy.friendogly.playground.dto.response;

import java.util.List;

public record FindPlaygroundSummaryResponse(

        Long playgroundId,
        int totalPetCount,
        int arrivedPetCount,
        List<String> petImageUrls
) {

}
