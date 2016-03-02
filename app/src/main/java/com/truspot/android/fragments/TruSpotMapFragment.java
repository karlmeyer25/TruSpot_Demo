package com.truspot.android.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.truspot.android.R;
import com.truspot.android.ui.PdmPinView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TruSpotMapFragment
        extends
            Fragment
        implements
            OnMapReadyCallback {

    // public constants
    public static final String BASIC_TAG = TruSpotMapFragment.class.getName();

    // private constants
    private final static String BUNDLE_KEY_MAP_STATE = "map_data";
    private static final LatLng USA = new LatLng(37.09024, -95.712891);
    private static final int DEFAULT_CAMERA_ZOOM = 15;

    // variables
    private GoogleMap mGoogleMap;

    // UI
    @Bind(R.id.mv_fragment_truspot)
    MapView mv;

    // get instance methods
    public static TruSpotMapFragment getInstance() {
        return new TruSpotMapFragment();
    }

    // methods
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_truspot, container, false);

        ButterKnife.bind(this, view);

        Bundle mapState = null;
        if (savedInstanceState != null) {
            // Load the map state bundle from the main savedInstanceState
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }

        mv.onCreate(mapState);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMapAsync();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mv != null) {
            mv.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mv != null) {
            mv.onPause();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mv != null) {
            mv.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the map state to it's own bundle
        Bundle mapState = new Bundle();

        if (mv != null) {
            mv.onSaveInstanceState(mapState);
        }

        // Put the map bundle in the main outState
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mv != null) {
            mv.onDestroy();
        }
    }

    private void getMapAsync() {
        mv.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);

        UiSettings settings = googleMap.getUiSettings();

        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                USA,
                DEFAULT_CAMERA_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);

        /*
        PdmPinView img = new PdmPinView(getActivity());
        img.setPadding(20, 20, 20, 20);
        img.setLayoutParams(new ViewGroup.LayoutParams(200, 200));

        Drawable d = img.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),
                d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);

        MarkerOptions markerOptions = new MarkerOptions().position(USA)
                .title("Current Location")
                .snippet("Thinking of finding some thing...")
                .icon(bd);

        Marker mMarker = googleMap.addMarker(markerOptions);
        */
    }
}
