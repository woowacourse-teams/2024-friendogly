package com.happy.friendogly.utils;

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
}
