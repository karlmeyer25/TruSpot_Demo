package com.truspot.android.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.truspot.android.R;
import com.truspot.android.fragments.TruSpotMapFragment;
import com.truspot.android.fragments.NearbyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = MainActivity.class.getName();

    private static final int TAB_MAP = 1;
    private static final int TAB_NEARBY = 2;

    // variables
    private TabsAdapter mTabsAdapter;

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

        setUiSettings();
        setAdapters();
    }

    private void setUiSettings() {
        setSupportActionBar(toolbar);
    }

    private void setAdapters() {
        mTabsAdapter = new TabsAdapter();

        vp.setAdapter(mTabsAdapter);

        tl.setupWithViewPager(vp);
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
