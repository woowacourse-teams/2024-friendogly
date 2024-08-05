package com.woowacourse.friendogly;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTest {

    @DisplayName("")
    @Test
    void fail() {
        Assertions.assertThat(1).isEqualTo(2);
    }
}
