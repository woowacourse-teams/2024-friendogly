package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.pet.domain.Gender;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubGender {

    @EmbeddedId
    private ClubGenderId clubGenderId;

    public ClubGender(Club club, Gender gender) {
        validateClub(club);
        validateGender(gender);
        this.clubGenderId = new ClubGenderId(club, gender);
    }

    private void validateClub(Club club) {
        if (club == null) {
            throw new FriendoglyException("모임 정보는 필수입니다.");
        }
    }

    private void validateGender(Gender gender) {
        if (gender == null) {
            throw new FriendoglyException("모임에 참여하는 반려견의 성별 정보는 필수입니다.");
        }
    }

    public boolean isSameGenderWith(Gender gender) {
        return this.getClubGenderId().getAllowedGender() == gender;
    }
}
