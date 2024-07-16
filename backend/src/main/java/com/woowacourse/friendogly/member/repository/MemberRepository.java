package com.woowacourse.friendogly.member.repository;

import com.woowacourse.friendogly.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
