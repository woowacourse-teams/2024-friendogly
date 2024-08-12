package com.happy.friendogly.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
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
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("이름 형식이 올바르지 않은 경우 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"1234567890123456"})
    @ParameterizedTest
    void create_Fail_IllegalNameFormat(String nameInput) {
        assertThatThrownBy(() -> Member.builder()
                .name(nameInput)
                .build()
        ).isInstanceOf(FriendoglyException.class);
    }
}
