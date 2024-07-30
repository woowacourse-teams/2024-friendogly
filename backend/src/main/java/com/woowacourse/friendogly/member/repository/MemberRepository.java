package com.woowacourse.friendogly.member.repository;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("회원 정보를 찾지 못했습니다."));
    }
}
