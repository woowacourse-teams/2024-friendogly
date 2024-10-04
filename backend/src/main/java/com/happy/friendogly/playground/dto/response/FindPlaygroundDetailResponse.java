package com.happy.friendogly.playground.dto.response;


import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import java.util.List;

public record FindPlaygroundDetailResponse(

        Long id,
        int totalPetCount,
        int arrivedPetCount,
        boolean isParticipating,
        List<PlaygroundPetDetail> playgroundPetDetails
) {

}
