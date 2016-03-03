package com.truspot.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.truspot.android.models.event.VenuesEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class NearbyFragment extends Fragment {

    // variables
    private EventBus mBus;

    // methods
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVariables();
    }

    private void initVariables() {
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    @Subscribe
    public void onEvent(VenuesEvent.StartLoading event) {
        // TODO add func
    }

    @Subscribe
    public void onEvent(VenuesEvent.CompleteLoading event) {
        // TODO add func
    }

    @Override
    public void onDestroy() {
        mBus.unregister(this);

        super.onDestroy();
    }
}
