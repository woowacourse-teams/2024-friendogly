package com.happy.friendogly.pet.repository;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("""
            SELECT p
            FROM Pet p
            JOIN FETCH p.member m
            WHERE p.id = :id
            """)
    Optional<Pet> findById(Long id);

    default Pet getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("강아지 정보를 찾지 못했습니다."));
    }

    @Query("""
            SELECT p
            FROM Pet p
            JOIN FETCH p.member m
            WHERE m.id = :memberId
            """)
    List<Pet> findByMemberId(Long memberId);

    Long countByMember(Member member);

    boolean existsByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);

    @Query("""
            SELECT p
            FROM Pet p
            WHERE p.member.id
            IN :memberIds
            """)
    List<Pet> findAllByMemberIds(List<Long> memberIds);
}
