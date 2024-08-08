package com.woowacourse.friendogly.notification.domain;

import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceToken;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public DeviceToken(Member member, String deviceToken) {
        this.member = member;
        this.deviceToken = deviceToken;
    }

    public void changeDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
