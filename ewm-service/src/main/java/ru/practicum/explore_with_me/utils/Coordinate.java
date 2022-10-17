package ru.practicum.explore_with_me.utils;

public class Coordinate {
    public static double distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double dist = 0;
        double radianLat1;
        double radianLat2;
        double theta;
        double radianTheta;
        double tolerance = 0.0001;
        if (Math.abs(latitude1 - latitude2) < tolerance && Math.abs(longitude1 - longitude2) < tolerance) {
            return dist;
        } else {
            radianLat1 = Math.PI * latitude1 / 180;
            radianLat2 = Math.PI * latitude2 / 180;
            theta = longitude1 - longitude2;
            radianTheta = Math.PI * theta / 180;
            dist = Math.sin(radianLat1) * Math.sin(radianLat2) + Math.cos(radianLat1)
                    * Math.cos(radianLat2) * Math.cos(radianTheta);

            if (dist > 1) {
                dist = 1;
            }

            dist = Math.acos(dist);
            dist = dist * 180 / Math.PI;
            return dist * 60 * 1.8524;
        }
    }
}

