package com.happy.friendogly.playground.domain;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.utils.GeoCalculator;
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

    private void validate(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new FriendoglyException("위도 범위가 올바르지 않습니다.");
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new FriendoglyException("경도 범위가 올바르지 않습니다.");
        }
    }

    public boolean isWithin(Location other, int radius) {
        double distance = GeoCalculator.calculateDistanceInMeters(latitude, longitude, other.latitude, other.longitude);
        return distance <= radius;
    }

    public Location findLocationWithLatitudeDiff(int meter) {
        double diffLatitude = GeoCalculator.calculateLatitudeOffset(this.latitude, meter);
        return new Location(diffLatitude, this.longitude);
    }

    public Location findLocationWithLongitudeDiff(double latitude, int meter) {
        double diffLongitude = GeoCalculator.calculateLongitudeOffset(latitude, this.longitude, meter);
        return new Location(this.latitude, diffLongitude);
    }
}
