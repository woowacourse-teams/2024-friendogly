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

    @Column(name = "address", nullable = false)
    private String value;

    public Address(String value) {
        validateAddress(value);
        this.value = value;
    }

    private void validateAddress(String address) {
        if (StringUtils.isBlank(address)) {
            throw new FriendoglyException("모임 주소는 필수입니다.");
        }
    }
}
