package at.htl.ecopoints.io;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.ecopoints.model.Location;

@Singleton
public class GpsSpeedCalculator {
    private static final String TAG = GpsSpeedCalculator.class.getSimpleName();
    private static final double EARTH_RADIUS = 6371e3; // Earth's radius in meters
    private static final double MS_TO_KMH = 3.6;
    private Location prevLocation = null;

    @Inject
    public GpsSpeedCalculator() {
    }

    public double calculateSpeed(Location currentLocation) {
        if (prevLocation == null) {
            prevLocation = currentLocation;
            return 0;
        }

        double distance = calculateDistance(currentLocation);
        double timeDiffInSeconds = (currentLocation.getDate().getTime() - prevLocation.getDate().getTime()) / 1000.0;

        if (timeDiffInSeconds == 0) {
            return 0;
        }

        double speedInMetersPerSecond = distance / timeDiffInSeconds;
        double speedInKmPerHour = speedInMetersPerSecond * MS_TO_KMH;
        prevLocation = currentLocation;
        return speedInKmPerHour;
    }

    private double calculateDistance(Location currentLocation) {
        double lat1 = Math.toRadians(prevLocation.getLatitude());
        double lon1 = Math.toRadians(prevLocation.getLongitude());
        double lat2 = Math.toRadians(currentLocation.getLatitude());
        double lon2 = Math.toRadians(currentLocation.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
