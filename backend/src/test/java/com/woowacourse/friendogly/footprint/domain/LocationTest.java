package com.woowacourse.friendogly.footprint.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class LocationTest {

    @DisplayName("올바른 위도, 경도 범위로 생성하면 예외가 발생하지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"90.000, 180.000", "-90.000, -180.000", "0.000, 0.000"})
    void constructor_Success(double latitude, double longitude) {
        assertThatCode(() -> new Location(latitude, longitude))
                .doesNotThrowAnyException();
    }

    @DisplayName("올바르지 않은 위도 범위로 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(doubles = {-90.001, 90.001})
    void constructor_Fail_IllegalLatitude(double latitude) {
        assertThatThrownBy(() -> new Location(latitude, 0.0))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("위도 범위가 올바르지 않습니다.");
    }

    @DisplayName("올바르지 않은 경도 범위로 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(doubles = {-180.001, 180.001})
    void constructor_Fail_IllegalLongitude(double longitude) {
        assertThatThrownBy(() -> new Location(0.0, longitude))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("경도 범위가 올바르지 않습니다.");
    }

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
