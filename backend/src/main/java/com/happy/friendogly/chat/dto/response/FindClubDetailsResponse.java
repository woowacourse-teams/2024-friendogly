package com.happy.friendogly.chat.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.util.Set;

public record FindClubDetailsResponse(
        Long clubId,
        Long myMemberId,
        Set<SizeType> allowedSizeTypes,
        Set<Gender> allowedGenders
) {

    public FindClubDetailsResponse(Long myMemberId, Club club) {
        this(club.getId(), myMemberId, club.getAllowedSizes(), club.getAllowedGenders());
    }
}
