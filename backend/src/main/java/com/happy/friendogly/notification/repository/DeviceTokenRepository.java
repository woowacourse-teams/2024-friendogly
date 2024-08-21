package com.happy.friendogly.notification.repository;

import com.happy.friendogly.notification.domain.DeviceToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByMemberId(Long memberId);

    @Query("""
            SELECT dt.deviceToken
            FROM DeviceToken dt
            INNER JOIN ChatRoomMember chatRoomMember ON dt.member.id = chatRoomMember.member.id
            WHERE chatRoomMember.chatRoom.id = :chatRoomId
            """)
    List<String> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    void deleteByMemberId(Long memberId);
}
