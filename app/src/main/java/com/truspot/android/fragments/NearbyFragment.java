package com.truspot.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.activities.VenueActivity;
import com.truspot.android.adapters.VenuesAdapter;
import com.truspot.android.comparators.DistanceComparator;
import com.truspot.android.interfaces.IGotData;
import com.truspot.android.interfaces.IGotPicasso;
import com.truspot.android.interfaces.IVenueClickListener;
import com.truspot.android.models.event.LocationEvent;
import com.truspot.android.models.event.MapSettingsEvent;
import com.truspot.android.models.event.VenuesEvent;
import com.truspot.android.ui.DividerItemDecoration;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.VenueFull;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NearbyFragment
        extends
            Fragment
        implements
        IVenueClickListener {

    // constants
    public static final String BASIC_TAG = NearbyFragment.class.getName();

    private static final int SHOW_RV = 1;
    private static final int SHOW_PROGRESS_VIEW = 2;
    private static final int SHOW_EMPTY = 3;

    // variables
    private EventBus mBus;
    private Location mMyLocation;
    private Picasso mPicasso;
    private IGotData mData;
    private VenuesAdapter mAdapter;
    private Map<VenueFull, LatLng> mLocationsMap;
    private int mMaxCapacity;
    private int mMapZoom;

    // UI
    @Bind(R.id.rv_fragment_nearby)
    RecyclerView rv;
    @Bind(R.id.pv_fragment_nearby)
    ProgressView pv;
    @Bind(R.id.tv_fragment_nearby_empty)
    TextView tvEmpty;

    // methods
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mPicasso = ((IGotPicasso) activity).getPicasso();
            mData = (IGotData) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " must implement " + IGotPicasso.class.getName() + " and " + IGotData.class.getName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVariables();
        setUiSettings();

        if (Util.isListNotEmpty(mData.getVenues())) {
            showVenues(mData.getVenues());
        } else {
            showView(SHOW_PROGRESS_VIEW);
        }
    }

/*
    @Subscribe
    public void onEvent(VenuesEvent.StartLoading event) {
        // TODO : this is never called. Analyze why.
    }
*/

    @Subscribe
    public void onEvent(VenuesEvent.CompleteLoading event) {
        showVenues(event.getVenues());
        mMaxCapacity = Util.findMaxVenueCapacity(event.getVenues());
    }

    @Subscribe
    public void onEvent(MapSettingsEvent.CompleteLoading event) {
        mMapZoom = event.getMs().getZoom();
    }

    private void showVenues(List<VenueFull> venues) {
        if (mMyLocation != null) {
            updateAdapter(venues);
        } else {
            resetAdapter(venues);
        }
    }

    @Subscribe
    public void onEvent(LocationEvent.LocationAvailable event) {
        mMyLocation = event.getLocation();

        if (mAdapter.getData() != null && mAdapter.getItemCount() > 0) {
            updateAdapter(mAdapter.getData());
        }

        final String TAG = Util.stringsToPath(BASIC_TAG, "onLocationChanged");
        LogUtil.log(TAG, String.format("Lat: %s, Lng: %s", mMyLocation.getLatitude(),
                mMyLocation.getLongitude()));
    }

    @Override
    public void onDestroy() {
        mBus.unregister(this);

        super.onDestroy();
    }

    @Override
    public void onVenueClick(VenueFull venueFull) {
        try {
            startActivity(VenueActivity.getIntent(getActivity(), venueFull, mMaxCapacity, mMapZoom));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        mBus = EventBus.getDefault();
        mBus.register(this);

        mAdapter = new VenuesAdapter(getActivity(), mPicasso, this, false);
        mLocationsMap = new HashMap<>();
    }

    private void setUiSettings() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        rv.setAdapter(mAdapter);
    }

    private void showView(int viewId) {
        pv.setVisibility(viewId == SHOW_PROGRESS_VIEW ? View.VISIBLE : View.GONE);
        rv.setVisibility(viewId == SHOW_RV ? View.VISIBLE : View.GONE);
        tvEmpty.setVisibility(viewId == SHOW_EMPTY ? View.VISIBLE : View.GONE);
    }

    private List<LatLng> getVenuesLocations(List<VenueFull> venuesFullList) {
        List<LatLng> locationsList = new ArrayList<>();

        for (VenueFull venueFull : venuesFullList) {
            locationsList.add(new LatLng(venueFull.getVenue().getLat(),
                    venueFull.getVenue().getLng()));
        }

        return locationsList;
    }

    private List<VenueFull> getSortedVenuesFullList(List<VenueFull> origList) {
        List<LatLng> locations = getVenuesLocations(origList);
        List<VenueFull> sortedVenuesList = new ArrayList<>();

        for (VenueFull venueFull : origList) {
            if (!mLocationsMap.containsKey(venueFull)) {
                mLocationsMap.put(venueFull, new LatLng(venueFull.getVenue().getLat(),
                        venueFull.getVenue().getLng()));
            }
        }

        if (mMyLocation != null) {
            Collections.sort(locations,
                    new DistanceComparator(new LatLng(mMyLocation.getLatitude(),
                            mMyLocation.getLongitude())));
        }

        for (LatLng latLng : locations) {
            for (Map.Entry<VenueFull, LatLng> entry : mLocationsMap.entrySet()) {
                if (entry.getValue().equals(latLng)) {
                    sortedVenuesList.add(entry.getKey());
                }
            }
        }

        return sortedVenuesList;
    }

    private void updateAdapter(List<VenueFull> origList) {
        List<VenueFull> sortedList = getSortedVenuesFullList(origList);
        resetAdapter(sortedList);
    }

    private void resetAdapter(List<VenueFull> venueFullList) {
        showView(SHOW_PROGRESS_VIEW);
        if (mAdapter != null) {
            mAdapter.clearData(true);
            mAdapter.addData(venueFullList, true);
        }

        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            showView(SHOW_RV);
        } else {
            showView(SHOW_EMPTY);
        }
    }
}
