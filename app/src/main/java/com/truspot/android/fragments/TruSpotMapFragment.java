package com.truspot.android.fragments;

import android.content.Intent;
import android.location.Location;
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
import com.rey.material.widget.FloatingActionButton;
import com.truspot.android.R;
import com.truspot.android.activities.SocialItemActivity;
import com.truspot.android.ui.PdmDrawable;
import com.truspot.android.utils.Util;

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
    private Location mCurrLocation;

    // UI
    @Bind(R.id.mv_fragment_truspot)
    MapView mv;
    @Bind(R.id.fab_fragment_truspot_my_location)
    FloatingActionButton fabMyLocation;

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

        initListeners();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // map settings
        UiSettings settings = mGoogleMap.getUiSettings();

        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);

        // enable my location
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mCurrLocation = location;
            }
        });

        // TODO : initial zoom to a place where venues are available. Remove it when needed.
        updateCamera(USA);

        // TODO: this is a test marker. Replace it when venues from db are available.
        PdmDrawable drawable = new PdmDrawable(getResources().getColor(R.color.pdm_border),
                getResources().getColor(R.color.accent),
                Util.convertDpiToPixels(getActivity(), 5));
        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(Util.convertToBitmap(drawable,
                Util.convertDpiToPixels(getActivity(), 50),
                Util.convertDpiToPixels(getActivity(), 50)));

        MarkerOptions markerOptions = new MarkerOptions().position(USA)
                .title("Current Location")
                .snippet("USA")
                .icon(bd);

        Marker mMarker = googleMap.addMarker(markerOptions);
    }

    private void initListeners() {
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : just for testing. Uncomment the lines below when done.
                Intent goToSocialItemActivity = SocialItemActivity.getIntent(getActivity());
                startActivity(goToSocialItemActivity);

                /*
                if (mCurrLocation != null) {
                    LatLng latLng = new LatLng(mCurrLocation.getLatitude(),
                            mCurrLocation.getLongitude());

                    updateCamera(latLng);
                }
                */
            }
        });
    }

    private void getMapAsync() {
        mv.getMapAsync(this);
    }

    private void updateCamera(LatLng location) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                location,
                DEFAULT_CAMERA_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
    }
}
