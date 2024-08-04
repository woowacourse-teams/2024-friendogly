package com.woowacourse.friendogly.auth.repository;

import com.woowacourse.friendogly.auth.domain.KakaoMember;
import com.woowacourse.friendogly.exception.FriendoglyException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long> {

    boolean existsByKakaoMemberId(Long kakaoMemberId);

    default KakaoMember getById(Long kakaoMemberId) {
        return findById(kakaoMemberId).orElseThrow(() -> new FriendoglyException("먼저 카카오 로그인이 완료되어야 합니다."));
    }
}
