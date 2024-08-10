package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "village", nullable = false)
    private String village;

    public Address(String province, String city, String village) {
        validateAddress(province, city, village);
        this.province = province;
        this.city = city;
        this.village = village;
    }

    private void validateAddress(String address, String city, String village) {
        if (StringUtils.isBlank(address) || StringUtils.isBlank(city) || StringUtils.isBlank(village)) {
            throw new FriendoglyException("모임 주소는 필수입니다.");
        }
    }
}
