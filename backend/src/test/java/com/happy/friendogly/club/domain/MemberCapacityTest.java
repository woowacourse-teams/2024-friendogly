package com.happy.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberCapacityTest {

    @DisplayName("모집 인원을 생성한다.")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest
    void create(int memberCapacityInput) {
        assertThatCode(() -> new MemberCapacity(memberCapacityInput))
                .doesNotThrowAnyException();
    }

    @DisplayName("모집 인원이 1명 미만 혹은 10명 초과이면 예외가 발생한다.")
    @ValueSource(ints = {0, 11})
    @ParameterizedTest
    void create_Fail(int memberCapacityInput) {
        assertThatThrownBy(() -> new MemberCapacity(memberCapacityInput))
                .isInstanceOf(FriendoglyException.class);
    }

}
