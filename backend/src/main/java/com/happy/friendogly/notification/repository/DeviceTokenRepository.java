package com.happy.friendogly.notification.repository;

import com.happy.friendogly.notification.domain.DeviceToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByMemberId(Long memberId);

    @Query("""
            SELECT dt.deviceToken
            FROM DeviceToken dt
            WHERE dt.member.id IN (SELECT chatRoomMember.member.id
            FROM ChatRoomMember chatRoomMember
            WHERE chatRoomMember.chatRoom.id = :chatRoomId)
            """)
    List<String> findAllByChatRoomId(Long chatRoomId);
}
