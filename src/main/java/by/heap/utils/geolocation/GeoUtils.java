package by.heap.utils.geolocation;

import by.heap.entity.geolocation.LatLng;

public final class GeoUtils {

    public GeoUtils() {
        throw new AssertionError();
    }

    public static boolean distanceBetweenPointsLessThen(LatLng pointA, LatLng pointB, double distanceInMeters) {
        return distanceBetweenPoints(pointA, pointB) < distanceInMeters;
    }

    public static double distanceBetweenPoints(LatLng pointA, LatLng pointB) {
        double R = 6371009d;
        double dLat = Math.toRadians(pointB.getLat() - pointA.getLat());
        double dLng = Math.toRadians(pointB.getLng() - pointA.getLng());
        double latA = Math.toRadians(pointA.getLat());
        double latB = Math.toRadians(pointB.getLat());

        // The actual haversine formula. a and c are well known value names in the formula.
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) *
                Math.cos(latA) * Math.cos(latB);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
