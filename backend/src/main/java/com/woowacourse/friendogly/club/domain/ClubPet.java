package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubPet {

    @EmbeddedId
    private ClubPetPk clubPetPk;

    @Builder
    public ClubPet(Club club, Pet pet) {
        validateClub(club);
        validatePet(pet);
        this.clubPetPk = new ClubPetPk(club, pet);
    }

    private void validateClub(Club club) {
        if (club == null) {
            throw new FriendoglyException("모임에 참여하는 회원 정보는 필수입니다.");
        }
    }

    private void validatePet(Pet pet) {
        if (pet == null) {
            throw new FriendoglyException("모임에 참여하는 회원의 반려견 정보는 필수입니다.");
        }
    }

    public void updateClub(Club club) {
        this.clubPetPk.updateClub(club);
    }

    public boolean isSameMember(Member member) {
        return this.clubPetPk.getPet().getMember().getId().equals(member.getId());
    }
}
