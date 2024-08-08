package com.woowacourse.friendogly.chat.repository;

import com.woowacourse.friendogly.chat.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
