package com.woowacourse.friendogly.chat.repository;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.exception.FriendoglyException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 ChatRoom ID입니다."));
    }
}
