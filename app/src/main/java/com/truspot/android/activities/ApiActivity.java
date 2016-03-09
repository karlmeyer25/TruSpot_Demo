package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.truspot.android.R;
import com.truspot.android.adapters.ApiAdapter;
import com.truspot.android.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApiActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = ApiActivity.class.getName();

    // variables
    private ApiAdapter mAdapter;

    // UI
    @Bind(R.id.toolbar_activity_api)
    Toolbar toolbar;
    @Bind(R.id.rv_activity_api)
    RecyclerView rv;

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
        setToolbarUiSettings();
        setupRecyclerView();
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

    private void initVariables() {
        mAdapter = new ApiAdapter(this, new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.api))));
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
    }
}
