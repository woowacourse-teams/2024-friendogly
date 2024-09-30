package com.happy.friendogly.playground.dto.response;


import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import java.util.List;

public record FindPlaygroundDetailResponse(

        Long id,
        long totalPetCount,
        long participatePetCount,
        List<PlaygroundPetDetail> playgroundPetDetails
) {

}
