package com.happy.friendogly.utils;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class GeoCalculator {

    private static final double ONE_DEGREE_LATITUDE_METER = 111000.0;
    private static final double EARTH_CIRCUMFERENCE_AT_EQUATOR_METER = 40075017;

    public static double calculateLatitudeOffset(double latitude, double distanceMeters) {
        double latitudeOffset = distanceMeters / ONE_DEGREE_LATITUDE_METER;
        return latitude + latitudeOffset;
    }

    public static double calculateLongitudeOffset(double latitude, double longitude, double distanceMeters) {
        double oneDegreeLongitudeMeter =
                Math.cos(Math.toRadians(latitude)) * (EARTH_CIRCUMFERENCE_AT_EQUATOR_METER / 360.0);

        double longitudeOffset = distanceMeters / oneDegreeLongitudeMeter;
        return longitude + longitudeOffset;
    }

    public static double calculateDistanceInMeters(
            double latitude,
            double longitude,
            double otherLatitude,
            double otherLongitude
    ) {
        double theta = longitude - otherLongitude;
        double dist = sin(toRadians(latitude)) * sin(toRadians(otherLatitude)) +
                cos(toRadians(latitude)) * cos(toRadians(otherLatitude)) * cos(toRadians(theta));

        dist = toDegrees(acos(dist));
        return degreeToMeter(dist);
    }

    private static double degreeToMeter(double dist) {
        return abs(dist * 60 * 1.1515 * 1609.344);
    }
}
