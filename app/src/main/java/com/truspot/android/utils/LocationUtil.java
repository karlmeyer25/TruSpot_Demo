package com.truspot.android.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtil {
    public static float calculateDistance(LatLng firstLatLng, LatLng secondLatLng) {
        //create customer location
        Location firstLocation = new Location("First");
        firstLocation.setLatitude(firstLatLng.latitude);
        firstLocation.setLongitude(firstLatLng.longitude);

        //create car location
        Location secondLocation = new Location("Second");
        secondLocation.setLatitude(secondLatLng.latitude);
        secondLocation.setLongitude(secondLatLng.longitude);

        return firstLocation.distanceTo(secondLocation) / 1000;
    }
}
