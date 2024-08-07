package com.woowacourse.friendogly.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_member_id", nullable = false)
    private String kakaoMemberId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public KakaoMember(String kakaoMemberId, Long memberId, String refreshToken) {
        this.kakaoMemberId = kakaoMemberId;
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
