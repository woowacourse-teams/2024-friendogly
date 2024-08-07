package com.woowacourse.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AddressTest {

    @DisplayName("모임 위치 생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> new Address("서울특별시", "송파구", "신천동"))
                .doesNotThrowAnyException();
    }

    @DisplayName("주소 정보 없이 생성하면 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void create_Fail_NullAndEmptyAddress(String addressInput) {
        assertThatThrownBy(() -> new Address(addressInput, "송파구", "신천동"))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임 주소는 필수입니다.");
    }
}
