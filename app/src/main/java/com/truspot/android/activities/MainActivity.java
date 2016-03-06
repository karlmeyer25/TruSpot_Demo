package com.truspot.android.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.fragments.NearbyFragment;
import com.truspot.android.fragments.TruSpotMapFragment;
import com.truspot.android.interfaces.GotPicasso;
import com.truspot.android.models.event.VenuesEvent;
import com.truspot.android.tasks.GetVenuesFullTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.utils.IntentUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.VenueFull;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity
        extends
            AppCompatActivity
        implements
            GotPicasso {

    // constants
    public static final String BASIC_TAG = MainActivity.class.getName();

    private static final int TAB_MAP = 1;
    private static final int TAB_NEARBY = 2;

    // variables
    private TabsAdapter mTabsAdapter;
    private EventBus mBus;
    private Picasso mPicasso;

    // UI
    @Bind(R.id.toolbar_activity_main)
    Toolbar toolbar;
    @Bind(R.id.tl_activity_main)
    TabLayout tl;
    @Bind(R.id.vp_activity_main)
    ViewPager vp;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initVariables();
        setUiSettings();
        setAdapters();

        if (Util.hasConnection(this)) {
            loadVenues();
        } else {
            showNoInternetDialog();
        }
    }

    private void initVariables() {
        mBus = EventBus.getDefault();
        mPicasso = Picasso.with(this);
    }

    private void setUiSettings() {
        setSupportActionBar(toolbar);
    }

    private void setAdapters() {
        mTabsAdapter = new TabsAdapter();

        vp.setAdapter(mTabsAdapter);

        tl.setupWithViewPager(vp);
    }

    private void showNoInternetDialog() {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(com.rey.material.R.style.Material_App_Dialog_Simple_Light);

        builder.message(getString(R.string.dialog_msg_no_internet_turn_it_on)).
                title(getString(R.string.dialog_title_warning)).
                positiveAction(getString(R.string.dialog_btn_go_to_internet_settings)).
                negativeAction(getString(R.string.dialog_btn_cancel));

        final Dialog dialog = builder.build(this);

        dialog.positiveActionClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                finish();

                startActivity(IntentUtil.getSettingsIntent());
            }

        }).negativeActionClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                finish();
            }}
        );

        dialog.show();
    }

    private void loadVenues() {
        final String TAG = Util.stringsToPath(BASIC_TAG, "loadVenues");

        new GetVenuesFullTask(new SimpleTask.SimpleCallback<List<VenueFull>>() {

            @Override
            public void onStart() {
                LogUtil.log(TAG, "onStart");

                mBus.post(new VenuesEvent.StartLoading());
            }

            @Override
            public void onComplete(List<VenueFull> res) {
/*
                if (res != null) {
                    for (VenueFull vf : res) {
                        try {
                            LogUtil.log(TAG, vf.toPrettyString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
*/
                mBus.post(new VenuesEvent.CompleteLoading(res));
            }

        }).execute();
    }

    @Override
    public Picasso getPicasso() {
        return mPicasso;
    }

    // inner classes
    private abstract class Tab {
        int id;
        String title;

        public Tab(int id, String title) {
            this.id = id;
            this.title = title;
        }

        abstract Fragment getFragment();
    }

    private class TabsAdapter extends FragmentPagerAdapter {
        Tab[] tabs;

        public TabsAdapter() {
            super(getSupportFragmentManager());

            tabs = new Tab[] {
                    new Tab(TAB_MAP, getString(R.string.tab_map)) {

                        @Override
                        Fragment getFragment() {
                            return TruSpotMapFragment.getInstance();
                        }

                    },
                    new Tab(TAB_NEARBY, getString(R.string.tab_nearby)) {

                        @Override
                        Fragment getFragment() {
                            return new NearbyFragment();
                        }

                    }
            };
        }

        @Override
        public Fragment getItem(int position) {
            return tabs[position].getFragment();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position].title;
        }

    }
}
