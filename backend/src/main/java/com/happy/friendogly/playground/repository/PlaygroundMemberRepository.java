package com.happy.friendogly.playground.repository;

import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundMemberRepository extends JpaRepository<PlaygroundMember, Long> {

    @EntityGraph(attributePaths = "member")
    List<PlaygroundMember> findAllByPlaygroundId(Long playgroundId);

    @EntityGraph(attributePaths = "member")
    List<PlaygroundMember> findAllByPlaygroundIdOrderByIsInsideDesc(Long playgroundId);

    boolean existsByPlaygroundIdAndMemberId(Long playgroundId, Long memberId);

    boolean existsByMemberId(Long memberId);

    Optional<PlaygroundMember> findByMemberId(Long memberId);

    boolean existsByPlaygroundId(Long playgroundId);
}
