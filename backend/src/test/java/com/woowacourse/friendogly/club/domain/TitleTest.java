package com.woowacourse.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TitleTest {

    @DisplayName("생성 테스트")
    @ValueSource(ints = {1, 100})
    @ParameterizedTest
    void create(int titleLength) {
        String titleInput = "a".repeat(titleLength);
        assertThatCode(() -> new Title(titleInput))
                .doesNotThrowAnyException();
    }

    @DisplayName("제목이 비어 있거나, 100글자 초과하여 생성하면 예외가 발생한다.")
    @ValueSource(ints = {0, 101})
    @ParameterizedTest
    void createFail(int titleLength) {
        String titleInput = "a".repeat(titleLength);

        assertThatThrownBy(() -> new Title(titleInput))
                .isInstanceOf(FriendoglyException.class);
    }
}
