package com.woowacourse.friendogly.pet.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GenderTest {

    @DisplayName("String 타입으로 Gender를 입력하면 Enum으로 변환한다.")
    @ValueSource(strings = {"MALE", "FEMALE", "MALE_NEUTERED", "FEMALE_NEUTERED"})
    @ParameterizedTest
    void toGender(String input) {
        assertThatCode(() -> Gender.toGender(input))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 Gender를 입력하면 예외가 발생한다.")
    @Test
    void toGender_Fail_InvalidGender() {
        String input = "INVALID_GENDER";

        assertThatThrownBy(() -> Gender.toGender(input))
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("존재하지 않는 Gender 입니다.");
    }
}
