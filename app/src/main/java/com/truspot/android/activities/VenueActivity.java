package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.Button;
import com.truspot.android.R;
import com.truspot.android.animations.ViewMover;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.android.utils.LocationUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
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

    private static final int MODE_NORMAL = 1;
    private static final int MODE_EXTENDED_MAP = 2;

    private static final int ANIM_DURATION = 400;

    // variables
    private VenueFull mVf;
    private GoogleMap mGoogleMap;
    private Resources mRes;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mContentLayoutKnown;
    private boolean mMapInitialStateReady;
    private int mContentWidth;
    private int mContentHeight;
    private int mToolbarHeight;
    private int mCurrMode;
    private ViewMover mViewMover;

    // UI
    @Bind(R.id.toolbar_activity_venue)
    Toolbar toolbar;
    @Bind(R.id.mv_activity_venue)
    MapView mv;
    @Bind(R.id.rl_activity_venue_container)
    RelativeLayout rlContainer;
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
        initVariables();
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

    @Override
    public void onBackPressed() {
        if (mCurrMode != MODE_NORMAL) {
            showMode(MODE_NORMAL, true);
        } else {
            super.onBackPressed();
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

    private void initVariables() {
        mRes = getResources();

        mScreenWidth = Util.getScreenDimensions(this).x;
        mScreenHeight = Util.getScreenDimensions(this).y;

        mCurrMode = MODE_NORMAL;
    }

    private void initListeners() {
        final String TAG = Util.stringsToPath(BASIC_TAG, "initListeners");

        btnShowSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mVf != null && mVf.getFeed() != null && mVf.getFeed().size() > 0) {
                        startActivity(SocialItemActivity.getIntent(VenueActivity.this, mVf));
                    } else {
                        Toast.makeText(VenueActivity.this,
                                "No social media added for this venue!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rlContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                LogUtil.log(TAG, "is called");

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rlContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                } else {
                    rlContainer.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                }

                mContentLayoutKnown = true;
                mContentWidth = rlContainer.getMeasuredWidth();
                mContentHeight = rlContainer.getMeasuredHeight();
                mToolbarHeight = toolbar.getMeasuredHeight();

                if (!mMapInitialStateReady) {
                    checkMapReadyForInit();
                }
            }
        });

        rlContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // consume all touch events, otherwise it will move the map
                return true;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        checkMapReadyForInit();
    }

    private void checkMapReadyForInit() {
        if (mGoogleMap != null &&
                mContentLayoutKnown) {
            mMapInitialStateReady = true;

            // init map settings
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    if (mCurrMode == MODE_NORMAL) {
                        showMode(MODE_EXTENDED_MAP, true);
                    }
                }

            });

            boolean isPortrait = Util.isPortraitOrientation(mRes);

            mViewMover = new ViewMover(ANIM_DURATION);
            mViewMover.addObjectToMove(
                    new ViewMover.MoverObject(
                            rlContainer,
                            0, 0,
                            isPortrait ? mContentHeight : 0,
                            isPortrait ? 0 : -mContentWidth)
            ).addObjectToMove(
                    new ViewMover.MoverObject(toolbar, 0, 0, -mToolbarHeight, 0));

            showMode(mCurrMode, false);
        }
    }

    private void showMode(int mode, boolean withAnimation) {
        mGoogleMap.clear();
        mCurrMode = mode;

        UiSettings uiSettings = mGoogleMap.getUiSettings();

        uiSettings.setCompassEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(mCurrMode == MODE_EXTENDED_MAP);
        uiSettings.setZoomControlsEnabled(mCurrMode == MODE_EXTENDED_MAP);

        mViewMover.move(mCurrMode == MODE_NORMAL, withAnimation);

        boolean isPortrait = Util.isPortraitOrientation(mRes);
        int area = isPortrait ? mScreenHeight : mScreenWidth;
        int notVisibleArea = mCurrMode == MODE_NORMAL ? (isPortrait ? mContentHeight : mContentWidth) : 0;

        mGoogleMap.setPadding(isPortrait ? 0 : notVisibleArea, 0, 0, isPortrait ? notVisibleArea : 0);

        LocationUtil.addVenueMarker(this, mGoogleMap, mVf.getVenue(), mVf.getVenue().getCapacity());

        LatLng venueLatLng = new LatLng(mVf.getVenue().getLat(), mVf.getVenue().getLng());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                venueLatLng,
                calculateZoom(mGoogleMap, area, notVisibleArea));

        if (withAnimation) {
            mGoogleMap.animateCamera(cameraUpdate, ANIM_DURATION, null);
        } else {
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    private float calculateZoom(GoogleMap map, int area, int notVisibleArea) {
        final String TAG = Util.stringsToPath(BASIC_TAG, "calculateZoom");
        float minZoomLevel = Math.max(map.getMinZoomLevel(), 9);
        float maxZoomLevel = Math.min(map.getMaxZoomLevel(), 15);
        float calculated = (((float) (area - notVisibleArea) / (float) area) * (maxZoomLevel - minZoomLevel)) + minZoomLevel;

        //LogUtil.log(TAG, String.format("maxZoomLevel: %s, calculated: %s", maxZoomLevel, calculated));

        return calculated;
    }

}
