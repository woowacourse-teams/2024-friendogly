package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.Status;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record FindSearchingClubResponse(
        Long id,
        String title,
        String content,
        String ownerMemberName,
        String address,
        Status status,
        LocalDateTime createdAt,
        Set<Gender> allowedGender,
        Set<SizeType> allowedSize,
        int memberCapacity,
        int currentMemberCount,
        String imageUrl,
        List<String> petImageUrls

) {

    public FindSearchingClubResponse(
            Club club,
            List<String> petImageUrls
    ) {
        this(
                club.getId(),
                club.getTitle().getValue(),
                club.getContent().getValue(),
                club.findOwnerName().getValue(),
                club.getAddress().getValue(),
                club.getStatus(),
                club.getCreatedAt(),
                club.getAllowedGenders(),
                club.getAllowedSizes(),
                club.getMemberCapacity().getValue(),
                club.countClubMember(),
                club.getImageUrl(),
                petImageUrls
        );
    }
}