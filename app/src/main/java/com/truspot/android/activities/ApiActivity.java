package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.truspot.android.R;
import com.truspot.android.adapters.VenuesAdapter;
import com.truspot.android.constants.Constants;
import com.truspot.android.tasks.AddVenueTask;
import com.truspot.android.tasks.DeleteVenueTask;
import com.truspot.android.tasks.GetVenuesFullTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.ui.DividerItemDecoration;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.Venue;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApiActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = ApiActivity.class.getName();

    private static final int SHOW_RV = 1;
    private static final int SHOW_PROGRESS_VIEW = 2;
    private static final int SHOW_EMPTY = 3;

    private static final int REQUEST_ADD_VENUE = 1;
    private static final int REQUEST_EDIT_VENUE = 2;

    // variables
    private VenuesAdapter mAdapter;

    // UI
    @Bind(R.id.toolbar_activity_api)
    Toolbar toolbar;
    @Bind(R.id.rv_activity_api)
    RecyclerView rv;
    @Bind(R.id.pv_activity_api)
    ProgressView pv;
    @Bind(R.id.tv_activity_api_empty)
    TextView tvEmpty;
    @Bind(R.id.fab_activity_api)
    FloatingActionButton fab;

    // get intent methods
    public static Intent getIntent(Context context) {
        return new Intent(context, ApiActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        ButterKnife.bind(this);

        initVariables();
        initListeners();
        setToolbarUiSettings();
        loadVenues();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ADD_VENUE || requestCode == REQUEST_EDIT_VENUE) &&
                resultCode == RESULT_OK) {
            Toast.makeText(ApiActivity.this,
                    requestCode == REQUEST_ADD_VENUE ?
                            "Venue successfully added!" : "Venue successfully updated!",
                    Toast.LENGTH_LONG).show();

            loadVenues();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.MENU_EDIT:
                try {
                    startActivityForResult(EditVenueActivity.getIntent(ApiActivity.this,
                            false, mAdapter.getItem(mAdapter.getPosition())), REQUEST_EDIT_VENUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.MENU_DELETE:
                new DeleteVenueTask(new SimpleTask.SimpleCallback<Venue>() {
                    @Override
                    public void onStart() {
                        showView(SHOW_PROGRESS_VIEW);
                    }

                    @Override
                    public void onComplete(Venue res) {
                        mAdapter.removeItem(mAdapter.getPosition(), true);

                        if (mAdapter.getItemCount() > 0) {
                            showView(SHOW_RV);
                        } else {
                            showView(SHOW_EMPTY);
                        }
                    }

                }, mAdapter.getItem(mAdapter.getPosition()).getVenue().getId()).execute();

                break;
        }

        return super.onContextItemSelected(item);
    }

    private void initVariables() {
        mAdapter = new VenuesAdapter(this, null, null, true);
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(EditVenueActivity.getIntent(ApiActivity.this, true, null),
                            REQUEST_ADD_VENUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        rv.setAdapter(mAdapter);
        registerForContextMenu(rv);

        if (mAdapter.getItemCount() > 0) {
            showView(SHOW_RV);
        } else {
            showView(SHOW_EMPTY);
        }
    }

    private void loadVenues() {
        final String TAG = Util.stringsToPath(BASIC_TAG, "loadVenues");

        new GetVenuesFullTask(new SimpleTask.SimpleCallback<List<VenueFull>>() {

            @Override
            public void onStart() {
                showView(SHOW_PROGRESS_VIEW);
            }

            @Override
            public void onComplete(List<VenueFull> res) {

                if (res != null) {
                    for (VenueFull vf : res) {
                        try {
                            LogUtil.log(TAG, vf.toPrettyString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (mAdapter.getItemCount() > 0) {
                    mAdapter.clearData(true);
                }

                mAdapter.addData(res, true);
                setupRecyclerView();
            }

        }).execute();
    }

    private void showView(int viewId) {
        pv.setVisibility(viewId == SHOW_PROGRESS_VIEW ? View.VISIBLE : View.GONE);
        rv.setVisibility(viewId == SHOW_RV ? View.VISIBLE : View.GONE);
        tvEmpty.setVisibility(viewId == SHOW_EMPTY ? View.VISIBLE : View.GONE);
    }
}
