package com.woowacourse.friendogly.chat.repository;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.exception.FriendoglyException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 ChatRoom ID입니다."));
    }

    @Query(value = """
            SELECT chatRoomMember.chatRoom
            FROM ChatRoomMember chatRoomMember
            WHERE chatRoomMember.member.id = :memberId
            """)
    List<ChatRoom> findMine(@Param("memberId") Long memberId);

    @Query(value = """
            SELECT new java.lang.Boolean(count(*) > 0)
            FROM ChatRoomMember chatRoomMember
            WHERE chatRoomMember.chatRoom.id = :chatRoomId AND chatRoomMember.member.id = :memberId
            """)
    boolean existsBy(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);
}
