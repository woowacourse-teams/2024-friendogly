package com.woowacourse.friendogly.chat.repository;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrivateChatRoomRepository extends JpaRepository<PrivateChatRoom, Long> {

    default PrivateChatRoom getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 PrivateChatRoom ID입니다."));
    }

    @Query(value = """
            SELECT room
            FROM PrivateChatRoom room
            WHERE (room.member.id = :memberId AND room.otherMember.id = :otherMemberId)
            OR (room.otherMember.id = :memberId AND room.member.id = :otherMemberId)
            """)
    Optional<PrivateChatRoom> findByTwoMemberIds(
            @Param("memberId") Long memberId,
            @Param("otherMemberId") Long otherMemberId
    );

    List<PrivateChatRoom> findByMemberOrOtherMember(Member member, Member otherMember);
}
