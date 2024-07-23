package com.woowacourse.friendogly.club.repository;

import com.woowacourse.friendogly.club.domain.Club;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    @EntityGraph(attributePaths = {"owner", "allowedGenders", "allowedSizes"})
    List<Club> findAll(Specification<Club> clubSpecification);
}
