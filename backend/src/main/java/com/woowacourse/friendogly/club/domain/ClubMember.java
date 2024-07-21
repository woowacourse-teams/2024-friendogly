package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "clubMember", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClubMemberPet> clubMemberPets = new ArrayList<>();

    @Builder
    public ClubMember(Club club, Member member) {
        validateClub(club);
        validateMember(member);
        this.club = club;
        this.member = member;
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

    public void addClubMemberPets(ClubMemberPet pet) {
        //TODO : 데려갈 수 있는 최대 반려견이 정해지면 예외처리
        //TODO : 해당 팻이 member가 키우는 팻인지 검증
        clubMemberPets.add(pet);
    }

    public String findOverviewPetImage() {
        if (clubMemberPets.isEmpty()) {
            return null;
        }
        return clubMemberPets.get(0).getPet().getImageUrl().getValue();
    }
}
