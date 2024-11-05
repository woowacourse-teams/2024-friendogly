package com.happy.friendogly.chatroom.repository;

import com.happy.friendogly.chatroom.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
