package com.happy.friendogly.footprint.repository;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.footprint.domain.Footprint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootprintRepository extends JpaRepository<Footprint, Long> {

    List<Footprint> findByCreatedAtAfter(LocalDateTime since);

    boolean existsByMemberIdAndCreatedAtAfter(Long memberId, LocalDateTime createdAt);

    Optional<Footprint> findTopOneByMemberIdOrderByCreatedAtDesc(Long memberId);

    default Footprint getTopOneByMemberIdOrderByCreatedAtDesc(Long memberId) {
        return findTopOneByMemberIdOrderByCreatedAtDesc(memberId)
                .orElseThrow(() -> new FriendoglyException("발자국이 존재하지 않습니다."));
    }

    List<Footprint> findAllByIsDeletedFalseAndCreatedAtAfter(LocalDateTime createdAt);

    default Footprint getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new FriendoglyException("발자국이 존재하지 않습니다."));
    }

    void deleteAllByMemberId(Long memberId);
}
