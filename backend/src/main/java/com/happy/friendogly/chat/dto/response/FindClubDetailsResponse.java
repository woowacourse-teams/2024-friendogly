package com.happy.friendogly.chat.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.util.Set;

public record FindClubDetailsResponse(
        Long clubId,
        Set<SizeType> allowedSizeTypes,
        Set<Gender> allowedGenders
) {

    public FindClubDetailsResponse(Club club) {
        this(club.getId(), club.getAllowedSizes(), club.getAllowedGenders());
    }
}
