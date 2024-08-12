package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
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

    private LocalDateTime createdAt;

    @Builder
    public ClubMember(Club club, Member member, LocalDateTime createdAt) {
        validateClub(club);
        validateMember(member);
        validateCreatedAt(createdAt);
        this.clubMemberPk = new ClubMemberPk(club, member);
        this.createdAt = createdAt;
    }

    public static ClubMember create(Club club, Member member) {
        return ClubMember.builder()
                .club(club)
                .member(member)
                .createdAt(LocalDateTime.now())
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

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new FriendoglyException("모임에 참여한 시각 정보는 필수입니다.");
        }
    }

    public void updateClub(Club club) {
        this.clubMemberPk.updateClub(club);
    }

    public boolean isSameMember(Member member) {
        return this.clubMemberPk.getMember().getId().equals(member.getId());
    }
}
