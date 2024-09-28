package com.happy.friendogly.playground.domain;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import com.happy.friendogly.exception.FriendoglyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;

    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    public Location(double latitude, double longitude) {
        validate(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isWithin(Location other, int radius) {
        return calculateDistanceInMeters(other) <= radius;
    }

    private double calculateDistanceInMeters(Location other) {
        double theta = this.longitude - other.longitude;
        double dist = sin(toRadians(this.latitude)) * sin(toRadians(other.latitude)) +
                cos(toRadians(this.latitude)) * cos(toRadians(other.latitude)) * cos(toRadians(theta));

        dist = toDegrees(acos(dist));
        return abs(dist * 60 * 1.1515 * 1609.344);
    }

    private void validate(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new FriendoglyException("위도 범위가 올바르지 않습니다.");
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new FriendoglyException("경도 범위가 올바르지 않습니다.");
        }
    }
}
