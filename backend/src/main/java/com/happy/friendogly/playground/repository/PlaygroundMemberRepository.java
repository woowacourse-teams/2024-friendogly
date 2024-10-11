package com.happy.friendogly.playground.repository;

import com.happy.friendogly.exception.FriendoglyException;
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

    @EntityGraph(attributePaths = {"playground", "member"})
    Optional<PlaygroundMember> findByMemberId(Long memberId);

    default PlaygroundMember getByMemberId(Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(() -> new FriendoglyException("멤버가 참여한 놀이터가 없습니다."));
    }

    boolean existsByPlaygroundId(Long playgroundId);
}
