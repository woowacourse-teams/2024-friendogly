package com.happy.friendogly.chatroom.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.util.Set;
import java.util.stream.Collectors;

public record FindClubDetailsResponse(
        Long clubId,
        Long myMemberId,
        Set<SizeType> allowedSizeTypes,
        Set<Gender> allowedGenders
) {

    public FindClubDetailsResponse(Long myMemberId, Club club) {
        this(
                club.getId(),
                myMemberId,
                club.getAllowedSizes().stream()
                        .map(clubSize -> clubSize.getClubSizeId().getAllowedSize())
                        .collect(Collectors.toSet()),
                club.getAllowedGenders().stream()
                        .map(clubGender -> clubGender.getClubGenderId().getAllowedGender())
                        .collect(Collectors.toSet())
        );
    }
}
