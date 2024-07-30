package com.woowacourse.friendogly.chat.repository;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatRoomRepository extends JpaRepository<PrivateChatRoom, Long> {

    Optional<PrivateChatRoom> findByMemberAndOtherMember(Member member, Member otherMember);

    boolean existsByMemberAndOtherMember(Member member, Member otherMember);
}
