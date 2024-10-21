package com.happy.friendogly.club.repository;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query("""
            SELECT C
            FROM Club AS C
            WHERE C.address.province = :province
                AND C.createdAt <= :lastFoundCreatedAt
                AND C.id < :lastFoundId
                AND EXISTS (
                    SELECT 1
                    FROM ClubGender CG
                    WHERE C = CG.clubGenderId.club AND CG.clubGenderId.allowedGender IN :searchingGenders
                )
                AND EXISTS (
                    SELECT 1
                    FROM ClubSize CS
                    WHERE C = CS.clubSizeId.club AND CS.clubSizeId.allowedSize IN :searchingSizes
                )
            ORDER BY C.createdAt DESC, C.id DESC 
            """)
    Slice<Club> findAllBy(
            String province,
            Set<Gender> searchingGenders,
            Set<SizeType> searchingSizes,
            Pageable pageable,
            LocalDateTime lastFoundCreatedAt,
            Long lastFoundId
    );

    @Query(value = """
            SELECT C
            FROM Club AS C
            JOIN FETCH C.clubPets AS CP
            JOIN FETCH CP.clubPetId.pet AS P
            WHERE C.id = :id
            """)
    Optional<Club> findById(Long id);

    default Club getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("모임 정보를 찾지 못했습니다."));
    }

    // TODO: ChatRoomMemberRepository에 있는 메서드로 대체 후 삭제
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
            JOIN ClubMember AS CM ON C.id = CM.clubMemberId.club.id
            WHERE CM.clubMemberId.member.id = :memberId
            ORDER BY C.createdAt DESC
            """
    )
    List<Club> findAllByParticipatingMemberId(@Param("memberId") Long memberId);
}
