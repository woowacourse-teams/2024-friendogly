package com.woowacourse.friendogly.club.repository;

import com.woowacourse.friendogly.club.domain.ClubPet;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPetRepository extends JpaRepository<ClubPet, Long> {

    @EntityGraph(attributePaths = {"club", "pet"})
    List<ClubPet> findAllByClubId(Long id);
}
