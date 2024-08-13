package com.happy.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContentTest {

    @DisplayName("생성 테스트")
    @ValueSource(ints = {1, 1000})
    @ParameterizedTest
    void create(int contentLength) {
        String contentInput = "a".repeat(contentLength);
        assertThatCode(() -> new Content(contentInput))
                .doesNotThrowAnyException();
    }

    @DisplayName("내용이 비어 있거나, 1000글자 초과하여 생성하면 예외가 발생한다.")
    @ValueSource(ints = {0, 1001})
    @ParameterizedTest
    void createFail(int contentLength) {
        String contentInput = "a".repeat(contentLength);

        assertThatThrownBy(() -> new Content(contentInput))
                .isInstanceOf(FriendoglyException.class);
    }

}
