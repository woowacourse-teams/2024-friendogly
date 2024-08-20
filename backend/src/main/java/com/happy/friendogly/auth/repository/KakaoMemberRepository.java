package com.happy.friendogly.auth.repository;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.exception.FriendoglyException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long> {

    Optional<KakaoMember> findByMemberId(Long memberId);

    Optional<KakaoMember> findByKakaoMemberId(String kakaoMemberId);

    Optional<KakaoMember> findByRefreshToken(String refreshToken);

    default KakaoMember getByRefreshToken(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .orElseThrow(() -> new FriendoglyException("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED));
    }

    default KakaoMember getByMemberId(Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 회원입니다.", HttpStatus.UNAUTHORIZED));
    }
}
