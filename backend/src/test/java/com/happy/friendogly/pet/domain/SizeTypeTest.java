package com.happy.friendogly.pet.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SizeTypeTest {

    @DisplayName("String 타입으로 SizeType을 입력하면 Enum으로 변환한다.")
    @ValueSource(strings = {"SMALL", "MEDIUM", "LARGE"})
    @ParameterizedTest
    void toSizeTypre(String input) {
        assertThatCode(() -> SizeType.toSizeType(input))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 SizeType을 입력하면 예외가 발생한다.")
    @Test
    void toSizeType_Fail_InvalidSizeType() {
        String input = "INVALID_SIZE_TYPE";

        assertThatThrownBy(() -> SizeType.toSizeType(input))
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("존재하지 않는 SizeType 입니다.");
    }
}
