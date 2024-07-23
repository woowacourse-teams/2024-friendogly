package com.woowacourse.friendogly.club.repository;

import com.woowacourse.friendogly.club.domain.ClubMember;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    boolean existsClubMemberByClubIdAndMemberId(Long clubId, Long memberId);

    @EntityGraph(attributePaths = {"club", "member", "clubMemberPets"})
    List<ClubMember> findAllByClubId(Long clubId);

    int countByClubId(Long id);
}

