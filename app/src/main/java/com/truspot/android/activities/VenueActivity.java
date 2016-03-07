package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.Button;
import com.truspot.android.R;
import com.truspot.android.constants.Constants;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.android.utils.LocationUtil;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class VenueActivity
        extends
            AppCompatActivity
        implements
            OnMapReadyCallback {
    // constants
    public static final String BASIC_TAG = VenueActivity.class.getName();

    private static final String BUNDLE_VF = "vf";
    private final static String BUNDLE_KEY_MAP_STATE = "venue_map_data";

    // variables
    private VenueFull mVf;
    private GoogleMap mGoogleMap;

    // UI
    @Bind(R.id.toolbar_activity_venue)
    Toolbar toolbar;
    @Bind(R.id.mv_activity_venue)
    MapView mv;
    @Bind(R.id.btn_activity_venue_show_social_media)
    Button btnShowSocialMedia;
    @Bind(R.id.tv_activity_venue_name)
    TextView tvName;
    @Bind(R.id.tv_activity_venue_description)
    TextView tvDescription;

    // static methods
    public static Intent getIntent(Context context, VenueFull vf) throws IOException {
        Intent intent = new Intent(context, VenueActivity.class);

        intent.putExtra(BUNDLE_VF, GoogleUtil.objectToJsonString(vf));

        return intent;
    }

    // methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        ButterKnife.bind(this);

        initExtras();
        initListeners();
        setToolbarUiSettings();
        setVenueUiSettings();
        loadMap(savedInstanceState);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void initExtras() {
        //final String TAG = Util.stringsToPath(BASIC_TAG, "initExtas");

        try {
            mVf = GoogleUtil.jsonToObject(VenueFull.class, getIntent().getStringExtra(BUNDLE_VF));

            //LogUtil.log(TAG, mVf.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        btnShowSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSocialItemActivity = SocialItemActivity.getIntent(VenueActivity.this);
                startActivity(goToSocialItemActivity);
            }
        });
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setVenueUiSettings() {
        tvName.setText(mVf.getVenue().getName());
        tvDescription.setText(mVf.getVenue().getDescription());
    }

    private void loadMap(Bundle savedInstanceState) {
        Bundle mapState = null;

        if (savedInstanceState != null) {
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }

        mv.onCreate(mapState);
        mv.getMapAsync(this);
    }

    private void loadVenueOnMap() {
        mGoogleMap.clear();

        LocationUtil.addVenueMarker(this, mGoogleMap, mVf.getVenue(), mVf.getVenue().getCapacity());

        LatLng venueLatLng = new LatLng(mVf.getVenue().getLat(), mVf.getVenue().getLng());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                venueLatLng,
                Constants.DEFAULT_CAMERA_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // map settings
        UiSettings settings = mGoogleMap.getUiSettings();

        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);

        loadVenueOnMap();
    }
}
