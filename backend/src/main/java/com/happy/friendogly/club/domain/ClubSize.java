package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.pet.domain.SizeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubSize {

    @EmbeddedId
    private ClubSizeId clubSizeId;

    public ClubSize(Club club, SizeType sizeType) {
        validateClub(club);
        validateSizeType(sizeType);
        this.clubSizeId = new ClubSizeId(club, sizeType);
    }

    private void validateClub(Club club) {
        if (club == null) {
            throw new FriendoglyException("모임 정보는 필수입니다.");
        }
    }

    private void validateSizeType(SizeType sizeType) {
        if (sizeType == null) {
            throw new FriendoglyException("모임에 참여하는 반려견의 크기 정보는 필수입니다.");
        }
    }

    public boolean hasSize(SizeType sizeType) {
        return this.getClubSizeId().getAllowedSize() == sizeType;
    }
}
