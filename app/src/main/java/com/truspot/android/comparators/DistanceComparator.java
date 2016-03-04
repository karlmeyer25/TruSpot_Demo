package com.truspot.android.comparators;

import com.google.android.gms.maps.model.LatLng;
import com.truspot.android.utils.LocationUtil;

import java.util.Comparator;

public class DistanceComparator implements Comparator<LatLng> {

    private LatLng mCurrLocation;

    public DistanceComparator(LatLng currLocation) {
        this.mCurrLocation = currLocation;
    }

    @Override
    public int compare(LatLng firstLocation, LatLng secondLocation) {
        float distance1 = LocationUtil.calculateDistance(firstLocation, mCurrLocation);
        float distance2 = LocationUtil.calculateDistance(secondLocation, mCurrLocation);

        return Float.valueOf(distance1).compareTo(distance2);
    }
}
