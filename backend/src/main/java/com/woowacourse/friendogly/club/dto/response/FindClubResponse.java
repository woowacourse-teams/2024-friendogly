package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.Status;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record FindClubResponse(
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
        String ownerImageUrl,
        boolean isMine,
        boolean alreadyParticipate,
        boolean canParticipate,
        List<ClubMemberDetailResponse> memberDetails,
        List<ClubPetDetailResponse> petDetails
) {

    public FindClubResponse(Club club, Member member, List<Pet> myPets) {
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
                club.findOwnerImageUrl(),
                club.isOwner(member),
                club.isAlreadyJoined(member),
                club.isJoinable(member, myPets),
                club.getClubMembers().stream()
                        .map(clubMember -> clubMember.getClubMemberPk().getMember())
                        .map(ClubMemberDetailResponse::new)
                        .toList(),
                club.getClubPets().stream()
                        .map(clubPet -> clubPet.getClubPetPk().getPet())
                        .map(pet -> new ClubPetDetailResponse(pet, pet.isOwner(member)))
                        .toList()
        );
    }
}
