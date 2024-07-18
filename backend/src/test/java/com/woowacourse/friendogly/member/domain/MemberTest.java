package com.woowacourse.friendogly.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> Member.builder()
                .name("누누")
                .email("crew@wooteco.com")
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("이메일 형식이 올바르지 않은 경우 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"www@.gmail", "@gmail.com"})
    @ParameterizedTest
    void create_Fail_IllegalEmailFormat(String emailInput) {
        assertThatThrownBy(() -> Member.builder()
                .name("누누")
                .email(emailInput)
                .build()
        ).isInstanceOf(FriendoglyException.class);
    }

    @DisplayName("이름 형식이 올바르지 않은 경우 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"1234567890123456"})
    @ParameterizedTest
    void create_Fail_IllegalNameFormat(String nameInput) {
        assertThatThrownBy(() -> Member.builder()
                .name(nameInput)
                .email("crew@wooteco.com")
                .build()
        ).isInstanceOf(FriendoglyException.class);
    }
}
