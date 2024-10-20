package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;
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

    @Column(name = "city")
    private String city;

    @Column(name = "village")
    private String village;

    public Address(String province, String city, String village) {
        validateProvince(province);
        this.province = province;
        this.city = city;
        this.village = village;
    }

    private void validateProvince(String province) {
        if (StringUtils.isBlank(province)) {
            throw new FriendoglyException("모임 주소는 필수입니다.");
        }
    }
}
