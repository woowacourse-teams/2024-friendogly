package com.woowacourse.friendogly.pet.repository;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Long countByMember(Member member);

    List<Pet> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    default Pet getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("강아지 정보를 찾지 못했습니다."));
    }
}
