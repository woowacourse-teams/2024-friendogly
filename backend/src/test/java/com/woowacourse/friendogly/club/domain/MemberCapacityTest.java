package com.woowacourse.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberCapacityTest {

    @DisplayName("모집 인원을 생성한다.")
    @Test
    void create() {
        assertThatCode(() -> new MemberCapacity(5))
                .doesNotThrowAnyException();
    }

    @DisplayName("모집 인원이 1명 미만 혹은 5명 초과이면 예외가 발생한다.")
    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void create_Fail(int memberCapacityInput) {
        assertThatThrownBy(() -> new MemberCapacity(memberCapacityInput))
                .isInstanceOf(FriendoglyException.class);
    }

}
