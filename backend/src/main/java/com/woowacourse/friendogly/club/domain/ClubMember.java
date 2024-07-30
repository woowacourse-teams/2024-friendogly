package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubMember {

    @EmbeddedId
    private ClubMemberPk clubMemberPk;

    @Builder
    public ClubMember(Club club, Member member) {
        validateClub(club);
        validateMember(member);
        this.clubMemberPk = new ClubMemberPk(club, member);
    }

    public static ClubMember create(Club club, Member member) {
        return ClubMember.builder()
                .club(club)
                .member(member)
                .build();
    }

    private void validateClub(Club club) {
        if (club == null) {
            throw new FriendoglyException("모임 정보는 필수입니다.");
        }
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new FriendoglyException("모임에 참여하는 회원 정보는 필수입니다.");
        }
    }

    public void updateClub(Club club) {
        this.clubMemberPk.updateClub(club);
    }

    public boolean isSameMember(Member member) {
        return this.clubMemberPk.getMember().getId().equals(member.getId());
    }
}
