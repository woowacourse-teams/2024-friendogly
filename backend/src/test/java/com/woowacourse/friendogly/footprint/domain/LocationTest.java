package com.woowacourse.friendogly.footprint.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocationTest {

    @DisplayName("약 999m 차이 나는 두 위치는 주변에 있다.")
    @Test
    void isNear_True() {
        Location location1 = new Location(0.0, 0.0);
        Location location2 = new Location(0.0, 0.008993216);
        assertThat(location1.isWithin(location2, 1000)).isTrue();
    }

    @DisplayName("약 1001m 차이 나는 두 위치는 주변에 있지 않다.")
    @Test
    void isNear_False() {
        Location location1 = new Location(0.0, 0.0);
        Location location2 = new Location(0.0, 0.009001209);
        assertThat(location1.isWithin(location2, 1000)).isFalse();
    }
}
