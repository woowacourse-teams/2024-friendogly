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
    List<ChatMessage> findAllByChatRoomIdAndCreatedAtAfterAndCreatedAtBeforeOrderByCreatedAtAsc(
            Long chatRoomId, LocalDateTime since, LocalDateTime until);

    default List<ChatMessage> findAllByTimeRange(Long chatRoomId, LocalDateTime since, LocalDateTime until) {
        return findAllByChatRoomIdAndCreatedAtAfterAndCreatedAtBeforeOrderByCreatedAtAsc(chatRoomId, since, until);
    }
}
