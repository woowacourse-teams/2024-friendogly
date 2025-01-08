package com.happy.friendogly.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.playground.domain.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GeoCalculatorTest {

    @DisplayName("특정 위도에서 300m 떨어진 위도를 구할 수 있다.")
    @Test
    void findFar300mLatitude() {
        // given
        double latitude = 37.516382;
        Location locationA = new Location(latitude, 10);

        // when
        double calculatedFar300mLatitude = GeoCalculator.calculateLatitudeOffset(latitude, 300);
        Location locationB = new Location(calculatedFar300mLatitude, 10);

        // then
        assertAll( // 오차범위가 존재하기 때문에 B가 A기준으로 299안에 없고, 301안에는 있는 것으로 검증하겠다.
                () -> assertThat(locationA.isWithin(locationB, 299)).isFalse(),
                () -> assertThat(locationA.isWithin(locationB, 301)).isTrue()
        );
    }

    @DisplayName("특정 경도에서 300m 떨어진 경도를 구할 수 있다.")
    @Test
    void findFar300mLongitude() {
        // given
        double latitude = 37.516382;
        double longitude = 127.120040;
        Location locationA = new Location(latitude, longitude);

        // when
        double calculatedFar300mLongitude = GeoCalculator.calculateLongitudeOffset(latitude, longitude, 300);
        Location locationB = new Location(latitude, calculatedFar300mLongitude);

        // then
        assertAll( // 오차범위가 존재하기 때문에 B가 A기준으로 299안에 없고, 301안에는 있는 것으로 검증하겠다.
                () -> assertThat(locationA.isWithin(locationB, 299)).isFalse(),
                () -> assertThat(locationA.isWithin(locationB, 301)).isTrue()
        );
    }
}
