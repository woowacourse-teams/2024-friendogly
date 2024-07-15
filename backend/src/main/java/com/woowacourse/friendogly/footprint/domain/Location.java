package com.woowacourse.friendogly.footprint.domain;

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

    private static final int BOUNDARY_RADIUS_METER = 1000;

    private double latitude;
    private double longitude;

    public boolean isNear(Location other) {
        return distance(this.latitude, this.longitude, other.latitude, other.longitude) <= BOUNDARY_RADIUS_METER;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2)) +
                      Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));

        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return Math.abs(dist);
    }

    private double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radToDeg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
