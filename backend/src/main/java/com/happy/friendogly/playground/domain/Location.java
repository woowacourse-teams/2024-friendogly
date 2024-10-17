package com.happy.friendogly.playground.domain;

import static com.happy.friendogly.playground.domain.Playground.MAX_OVERLAP_DISTANCE;

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

    public Location plusLatitudeByOverlapDistance() {
        double diffLatitude = GeoCalculator.calculateLatitudeOffset(latitude, MAX_OVERLAP_DISTANCE);
        return new Location(diffLatitude, longitude);
    }

    public Location minusLatitudeByOverlapDistance() {
        double diffLatitude = GeoCalculator.calculateLatitudeOffset(latitude, -MAX_OVERLAP_DISTANCE);
        return new Location(diffLatitude, longitude);
    }

    public Location plusLongitudeByOverlapDistance() {
        double diffLongitude = GeoCalculator.calculateLongitudeOffset(latitude, longitude, MAX_OVERLAP_DISTANCE);
        return new Location(latitude, diffLongitude);
    }

    public Location minusLongitudeByOverlapDistance() {
        double diffLongitude = GeoCalculator.calculateLongitudeOffset(latitude, longitude, -MAX_OVERLAP_DISTANCE);
        return new Location(latitude, diffLongitude);
    }
}
