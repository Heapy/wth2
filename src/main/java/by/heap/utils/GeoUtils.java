package by.heap.utils;

public final class GeoUtils {

    private GeoUtils() {
    }

    public static boolean distanceBetweenPointsLessThen(String latA, String lngA, String latB, String lngB, double distanceInKilometers) {
        return distanceBetweenPoints(Double.valueOf(latA), Double.valueOf(lngA),Double.valueOf(latB), Double.valueOf(lngB)) / 1000 < distanceInKilometers;
    }

    private static double distanceBetweenPoints(double latA, double lngA, double latB, double lngB) {
        double R = 6371009d;
        double dLat = Math.toRadians(latB - latA);
        double dLng = Math.toRadians(lngB - lngA);
        double radLatA = Math.toRadians(latA);
        double radLatB = Math.toRadians(latB);

        // The actual haversine formula. a and c are well known value names in the formula.
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) *
                Math.cos(radLatB) * Math.cos(radLatA);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
