package com.woowacourse.friendogly.footprint.domain;

import static java.lang.Math.*;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Location {

    private double latitude;
    private double longitude;

    public boolean isWithin(Location other, int radius) {
        return distanceAsMeter(other) <= radius;
    }

    private double distanceAsMeter(Location other) {
        double theta = this.longitude - other.longitude;
        double dist = sin(toRadians(this.latitude)) * sin(toRadians(other.latitude)) +
                      cos(toRadians(this.latitude)) * cos(toRadians(other.latitude)) * cos(toRadians(theta));

        dist = toDegrees(acos(dist));
        return abs(dist * 60 * 1.1515 * 1609.344);
    }
}
