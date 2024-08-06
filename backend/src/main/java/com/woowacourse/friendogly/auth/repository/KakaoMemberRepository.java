package com.woowacourse.friendogly.auth.repository;

import com.woowacourse.friendogly.auth.domain.KakaoMember;
import com.woowacourse.friendogly.exception.FriendoglyException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long> {

    Optional<KakaoMember> findByKakaoMemberId(String KakaoMemberId);

    Optional<KakaoMember> findByRefreshToken(String refreshToken);

    default KakaoMember getByRefreshToken(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .orElseThrow(() -> new FriendoglyException("리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED));
    }
}
