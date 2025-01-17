package com.happy.friendogly.chatroom.repository;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.exception.FriendoglyException;
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
}
