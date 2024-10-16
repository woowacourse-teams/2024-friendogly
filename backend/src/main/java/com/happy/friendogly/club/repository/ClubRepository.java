package com.happy.friendogly.club.repository;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.exception.FriendoglyException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    @EntityGraph(value = "graph.Club")
    List<Club> findAll(Specification<Club> clubSpecification);

    @EntityGraph(value = "graph.Club")
    Optional<Club> findById(Long id);

    default Club getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("모임 정보를 찾지 못했습니다."));
    }

    @Query(value = """
            SELECT new java.lang.Boolean(count(*) > 0)
            FROM ClubMember clubMember
            WHERE clubMember.clubMemberId.club.chatRoom.id = :chatRoomId
                AND clubMember.clubMemberId.member.id = :memberId
            """)
    boolean existsBy(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

    Optional<Club> findByChatRoomId(Long chatRoomId);

    default Club getByChatRoomId(Long chatRoomId) {
        return findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new FriendoglyException("해당 채팅방에 해당하는 모임이 존재하지 않습니다."));
    }

    @Query(value = """
                SELECT C
                FROM Club AS C
                JOIN FETCH C.allowedGenders
                JOIN FETCH C.allowedSizes
                JOIN FETCH C.clubMembers AS CM
                JOIN FETCH CM.clubMemberId.member AS M
                JOIN FETCH C.clubPets AS CP
                JOIN FETCH CP.clubPetId.pet
                WHERE C.id IN (
                    SELECT C2.id
                    FROM Club AS C2
                    JOIN ClubMember AS CM2 ON CM2.clubMemberId.club = C2
                    JOIN Member AS M2 ON CM2.clubMemberId.member = M2
                    where M2.id = :memberId
                )
                ORDER BY C.createdAt DESC
            """)
    List<Club> findAllByParticipatingMemberId(@Param("memberId") Long memberId);
}
