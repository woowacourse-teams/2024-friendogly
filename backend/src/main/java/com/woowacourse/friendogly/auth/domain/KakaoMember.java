package com.woowacourse.friendogly.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoMember {

    @Id
    private Long kakaoMemberId;

    @Column(name = "member_id")
    private Long memberId;

    public KakaoMember(Long kakaoMemberId) {
        this.kakaoMemberId = kakaoMemberId;
    }

    public void updateMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
