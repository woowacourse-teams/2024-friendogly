package com.happy.friendogly.chat.repository;

import com.happy.friendogly.chat.domain.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @EntityGraph(attributePaths = {"senderMember"})
    List<ChatMessage> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    @EntityGraph(attributePaths = {"senderMember"})
    List<ChatMessage> findAllByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(Long chatRoomId, LocalDateTime since);

    default List<ChatMessage> findRecent(Long chatRoomId, LocalDateTime since) {
        return findAllByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoomId, since);
    }
}
