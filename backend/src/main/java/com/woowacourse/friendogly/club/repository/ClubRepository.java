package com.woowacourse.friendogly.club.repository;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.exception.FriendoglyException;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    @EntityGraph(attributePaths = {"allowedGenders", "allowedSizes"})
    List<Club> findAll(Specification<Club> clubSpecification);

    default Club getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("모임 정보를 찾지 못했습니다."));
    }
}
