package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ClubMember(Club club, Member member, LocalDateTime createdAt) {
        validateClub(club);
        validateMember(member);
        validateCreateAt(createdAt);
        this.club = club;
        this.member = member;
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

    private void validateCreateAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new FriendoglyException("방에 참가한 시간은 필수입니다.");
        }
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new FriendoglyException("모임에 참여하는 회원 정보는 필수입니다.");
        }
    }
}
