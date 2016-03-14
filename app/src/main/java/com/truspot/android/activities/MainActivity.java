package com.truspot.android.activities;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.adapters.SuggestionAdapter;
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

import java.io.IOException;
import java.util.ArrayList;
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
    private SuggestionAdapter mSuggestionAdapter;
    private EventBus mBus;
    private Picasso mPicasso;
    private SearchManager mSearchManager;
    private List<VenueFull> mVenuesList;

    // UI
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchAutoCompleteView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(mSearchManager.getSearchableInfo(MainActivity.this.getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_edit: {
                startActivity(ApiActivity.getIntent(this));

                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void initVariables() {
        mBus = EventBus.getDefault();
        mPicasso = Picasso.with(this);
        mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
                                                         }
                                                     }
        );

        dialog.show();
    }

    private void loadVenues() {
        final String TAG = Util.stringsToPath(BASIC_TAG, "loadVenues");

        new GetVenuesFullTask(new SimpleTask.SimpleCallback<List<VenueFull>>() {

            @Override
            public void onStart() {
                LogUtil.log(TAG, "onStart");

                // TODO : this event is never posted on its subscribers...
                mBus.post(new VenuesEvent.StartLoading());
            }

            @Override
            public void onComplete(List<VenueFull> res) {
                mBus.post(new VenuesEvent.CompleteLoading(res));

                mVenuesList = res;

                mSuggestionAdapter = new SuggestionAdapter(MainActivity.this,
                        R.layout.item_search,
                        getVenueNames(res));

                initSearchAutocompleteView();
            }

        }).execute();
    }

    private void initSearchAutocompleteView() {
        searchAutoCompleteView = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoCompleteView.setThreshold(1);
        searchAutoCompleteView.setAdapter(mSuggestionAdapter);

        searchAutoCompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    VenueFull venueFull = findVenueByName(((TextView) view).getText().toString());
                    if (venueFull != null) {
                        try {
                            startActivity(VenueActivity.getIntent(MainActivity.this, venueFull));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private ArrayList<String> getVenueNames(List<VenueFull> venueFullList) {
        ArrayList<String> venueNames = new ArrayList<>();

        for (VenueFull venue : venueFullList) {
            venueNames.add(venue.getVenue().getName());
        }

        return venueNames;
    }

    private VenueFull findVenueByName(String name) {
        for (VenueFull venue : mVenuesList) {
            if (name.toLowerCase().equals(venue.getVenue().getName().toLowerCase())) {
                return venue;
            }
        }

        return null;
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
            super(getFragmentManager());

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
