package com.woowacourse.friendogly.pet.repository;

import com.woowacourse.friendogly.pet.domain.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByMemberId(Long memberId);
}
