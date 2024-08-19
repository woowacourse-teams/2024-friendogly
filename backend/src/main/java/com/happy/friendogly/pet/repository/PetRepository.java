package com.happy.friendogly.pet.repository;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Long countByMember(Member member);

    List<Pet> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);

    default Pet getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("강아지 정보를 찾지 못했습니다."));
    }
}
