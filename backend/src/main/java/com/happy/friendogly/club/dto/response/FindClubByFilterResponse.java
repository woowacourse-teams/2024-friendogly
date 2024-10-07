package com.happy.friendogly.club.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.Status;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record FindClubByFilterResponse(
        Long id,
        String title,
        String content,
        String ownerMemberName,
        AddressDetailResponse address,
        Status status,
        LocalDateTime createdAt,
        Set<Gender> allowedGender,
        Set<SizeType> allowedSize,
        int memberCapacity,
        int currentMemberCount,
        String imageUrl,
        List<String> petImageUrls
) {

    public FindClubByFilterResponse(
            Club club,
            List<String> petImageUrls
    ) {
        this(
                club.getId(),
                club.getTitle().getValue(),
                club.getContent().getValue(),
                club.findOwnerName().getValue(),
                new AddressDetailResponse(club.getAddress()),
                club.getStatus(),
                club.getCreatedAt(),
                club.getAllowedGenders().stream()
                        .map(clubGender -> clubGender.getClubGenderId().getAllowedGender())
                        .collect(Collectors.toSet()),
                club.getAllowedSizes().stream()
                        .map(clubSize -> clubSize.getClubSizeId().getAllowedSize())
                        .collect(Collectors.toSet()),
                club.getMemberCapacity().getValue(),
                club.countClubMember(),
                club.getImageUrl(),
                petImageUrls
        );
    }
}
