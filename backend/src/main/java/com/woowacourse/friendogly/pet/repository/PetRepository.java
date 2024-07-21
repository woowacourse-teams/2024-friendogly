package com.woowacourse.friendogly.pet.repository;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Long countByMember(Member member);

    List<Pet> findByMemberId(Long memberId);
}
