package com.truspot.android.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.truspot.android.ui.PdmDrawable;
import com.truspot.backend.api.model.Venue;

public class LocationUtil {

    // constants
    private static final int MAX_PDM_SIZE_DP = 30;
    private static final int MIN_PDM_SIZE_DP = 10;

    // methods
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

    public static Marker addVenueMarker(Context context,
                                        GoogleMap googleMap,
                                        Venue venue,
                                        int maxCapacity) {

        int origColor = Color.parseColor(venue.getPdmColor());
        int secColor = ColorUtil.adjustAlpha(origColor, 0.25f);

        int sizeDp = (int) (MAX_PDM_SIZE_DP * ((double) venue.getCapacity() / (double) maxCapacity));

        if (sizeDp < MIN_PDM_SIZE_DP) {
            sizeDp = MIN_PDM_SIZE_DP;
        }

        int radiusDp = sizeDp / 2;
        int occupancyRadiusDp = (int) (radiusDp * ((double) venue.getOccupancy() / (double) venue.getCapacity()));
        int borderWidthDp = radiusDp - occupancyRadiusDp;

        PdmDrawable drawable = new PdmDrawable(secColor,
                origColor,
                Util.convertDpiToPixels(context, borderWidthDp));

        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(
                Util.convertToBitmap(
                        drawable,
                        Util.convertDpiToPixels(context, sizeDp),
                        Util.convertDpiToPixels(context, sizeDp)));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(venue.getLat(), venue.getLng()))
                .title(venue.getName())
                .snippet(venue.getDescription())
                .icon(bd);

        return googleMap.addMarker(markerOptions);
    }
}
