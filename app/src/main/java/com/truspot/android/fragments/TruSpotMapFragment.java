package com.truspot.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.rey.material.widget.FloatingActionButton;
import com.truspot.android.R;
import com.truspot.android.activities.VenueActivity;
import com.truspot.android.constants.Constants;
import com.truspot.android.interfaces.IGotData;
import com.truspot.android.models.event.LocationEvent;
import com.truspot.android.models.event.MapSettingsEvent;
import com.truspot.android.models.event.VenuesEvent;
import com.truspot.android.utils.LocationUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.MapSettings;
import com.truspot.backend.api.model.Venue;
import com.truspot.backend.api.model.VenueFull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TruSpotMapFragment
        extends
            Fragment
        implements
            OnMapReadyCallback,
            GoogleMap.OnMarkerClickListener {

    // public constants
    public static final String BASIC_TAG = TruSpotMapFragment.class.getName();

    // private constants
    private static final LatLng USA = new LatLng(37.09024, -95.712891);

    // variables
    private IGotData mData;
    private GoogleMap mGoogleMap;
    private EventBus mBus;
    private Location mCurrLocation;
    private List<VenueFull> mVenues;
    private MapSettings mMapSettings;
    private HashMap<String, VenueFull> mMarkerVenueMap;
    private boolean mVenuesLoaded;
    private int mMaxCapacity;

    // UI
    @Bind(R.id.mv_fragment_truspot_map)
    MapView mv;
    @Bind(R.id.fl_fragment_truspot_map_progress)
    FrameLayout flProgress;
    @Bind(R.id.fab_fragment_truspot_map_my_location)
    FloatingActionButton fabMyLocation;
    @Bind(R.id.tv_fragment_truspot_map_place_info)
    TextView tvPlaceInfo;

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

        mv.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mData = (IGotData) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " must implement " + IGotData.class.getName());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVariables();
        initListeners();
        getMapAsync();

        if (Util.isListNotEmpty(mData.getVenues())) {
            mVenues = mData.getVenues();
        } else {
            showProgress();
        }

        mMapSettings = mData.getMapSettings();
    }

    @Override
    public void onResume() {
        super.onResume();

        mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mv.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mv.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBus.unregister(this);

        mv.onDestroy();
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

                    updateCamera(latLng, Constants.DEFAULT_CAMERA_ZOOM);
                }
            }
        });
    }

/*
    @Subscribe
    public void onEvent(VenuesEvent.StartLoading event) {
        LogUtil.log(BASIC_TAG, "start loading");

        // TODO : not called
    }
*/

    @Subscribe
    public void onEvent(VenuesEvent.CompleteLoading event) {
        mVenues = event.getVenues();

        tryLoadVenuesOnMap();
    }

    @Subscribe
    public void onEvent(MapSettingsEvent.CompleteLoading event) {
        mMapSettings = event.getMs();

        tryLoadMapSettings();
    }

    private void showProgress() {
        flProgress.setVisibility(View.VISIBLE);
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
        settings.setZoomControlsEnabled(true);

        // enable my location
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mCurrLocation = location;
                mBus.post(new LocationEvent.LocationAvailable(location));
                tvPlaceInfo.setText(getAddressAsString(location.getLatitude(), location.getLongitude()));
            }
        });

        //mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);

        updateCamera(USA, Constants.DEFAULT_CAMERA_ZOOM);

        tryLoadVenuesOnMap();
        tryLoadMapSettings();
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

        //LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        if (mMarkerVenueMap != null) {
            mMarkerVenueMap.clear();
        }

        mMarkerVenueMap = new HashMap<>();
        mMaxCapacity = Util.findMaxVenueCapacity(mVenues);

        for (VenueFull vf : mVenues) {
            Venue venue = vf.getVenue();

            Marker marker = LocationUtil.addVenueMarker(getActivity(), mGoogleMap, venue, mMaxCapacity);

            mMarkerVenueMap.put(marker.getId(), vf);

            //boundsBuilder.include(new LatLng(venue.getLat(), venue.getLng()));
        }

        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), Util.convertDpiToPixels(getActivity(), 20)));

        flProgress.setVisibility(View.GONE);
    }

    private void tryLoadMapSettings() {
        if (mGoogleMap != null && mMapSettings != null) {
            if (mMapSettings.getLat() != null && mMapSettings.getLng() != null) {
                updateCamera(new LatLng(mMapSettings.getLat(), mMapSettings.getLng()), mMapSettings.getZoom());
            }/* else {
                updateCamera(USA, Constants.DEFAULT_CAMERA_ZOOM);
            }*/
        }
    }

    private void updateCamera(LatLng location, int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                location,
                zoom);

        mGoogleMap.moveCamera(cameraUpdate);
    }

    private String getAddressAsString(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String finalAddress = builder.toString();
            tvPlaceInfo.setVisibility(View.VISIBLE);

            return String.format("Your location : %s", finalAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            startActivity(VenueActivity.getIntent(getActivity(),
                    mMarkerVenueMap.get(marker.getId()),
                    mMaxCapacity,
                    mMapSettings != null ? mMapSettings.getZoom() : 0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
