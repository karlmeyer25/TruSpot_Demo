package com.truspot.android.fragments;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.rey.material.widget.FloatingActionButton;
import com.truspot.android.R;
import com.truspot.android.activities.VenueActivity;
import com.truspot.android.constants.Constants;
import com.truspot.android.models.event.LocationEvent;
import com.truspot.android.models.event.VenuesEvent;
import com.truspot.android.utils.LocationUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.Venue;
import com.truspot.backend.api.model.VenueFull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TruSpotMapFragment
        extends
            Fragment
        implements
            OnMapReadyCallback,
            GoogleMap.OnInfoWindowClickListener {

    // public constants
    public static final String BASIC_TAG = TruSpotMapFragment.class.getName();

    // private constants
    private final static String BUNDLE_KEY_MAP_STATE = "map_data";
    private static final LatLng USA = new LatLng(37.09024, -95.712891);

    // variables
    private GoogleMap mGoogleMap;
    private EventBus mBus;
    private Location mCurrLocation;
    private List<VenueFull> mVenues;
    private HashMap<String, VenueFull> mMarkerVenueMap;
    private boolean mVenuesLoaded;

    // UI
    @Bind(R.id.mv_fragment_truspot_map)
    MapView mv;
    @Bind(R.id.fl_fragment_truspot_map_progress)
    FrameLayout flProgress;
    @Bind(R.id.fab_fragment_truspot_map_my_location)
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
        View view = inflater.inflate(R.layout.fragment_truspot_map, container, false);

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

        initVariables();
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

        mBus.unregister(this);

        if (mv != null) {
            mv.onDestroy();
        }
    }

    private void initVariables() {
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    private void initListeners() {
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrLocation != null) {
                    LatLng latLng = new LatLng(mCurrLocation.getLatitude(),
                            mCurrLocation.getLongitude());

                    updateCamera(latLng);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(VenuesEvent.StartLoading event) {
        LogUtil.log(BASIC_TAG, "start loading");

        flProgress.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(VenuesEvent.CompleteLoading event) {
        mVenues = event.getVenues();

        tryLoadVenuesOnMap();
    }

    private void getMapAsync() {
        mv.getMapAsync(this);
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
                mBus.post(new LocationEvent.LocationAvailable(location));
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(this);

        updateCamera(USA);

        tryLoadVenuesOnMap();
    }

    private void tryLoadVenuesOnMap() {
        if (mGoogleMap != null && mVenues != null && !mVenuesLoaded) {
            loadVenuesOnMap();
        }
    }

    private void loadVenuesOnMap() {
        final String TAG = Util.stringsToPath(BASIC_TAG, "loadVenuesOnMap");

        LogUtil.log(TAG, "called");

        mVenuesLoaded = true;

        mGoogleMap.clear();

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        if (mMarkerVenueMap != null) {
            mMarkerVenueMap.clear();
        }

        mMarkerVenueMap = new HashMap<>();
        int maxCapacity = findMaxCapacity();

        for (VenueFull vf : mVenues) {
            Venue venue = vf.getVenue();

            Marker marker = LocationUtil.addVenueMarker(getActivity(), mGoogleMap, venue, maxCapacity);

            mMarkerVenueMap.put(marker.getId(), vf);

            boundsBuilder.include(new LatLng(venue.getLat(), venue.getLng()));
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), Util.convertDpiToPixels(getActivity(), 20)));

        flProgress.setVisibility(View.GONE);
    }

    private int findMaxCapacity() {
        int max = 0;

        for (VenueFull vf : mVenues) {
            if (vf.getVenue().getCapacity() > max) {
                max = vf.getVenue().getCapacity();
            }
        }

        return max;
    }

    private void updateCamera(LatLng location) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                location,
                Constants.DEFAULT_CAMERA_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            startActivity(VenueActivity.getIntent(getActivity(), mMarkerVenueMap.get(marker.getId())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
