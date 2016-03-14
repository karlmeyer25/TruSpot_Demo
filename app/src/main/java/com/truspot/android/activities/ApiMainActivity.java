package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.truspot.android.R;
import com.truspot.android.ui.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yavoryordanov on 3/14/16.
 */
public class ApiMainActivity extends AppCompatActivity {
    // constants
    public static final String BASIC_TAG = ApiMainActivity.class.getName();

    // variables

    // UI
    @Bind(R.id.toolbar_activity_api_main)
    Toolbar toolbar;
    @Bind(R.id.rv_activity_api_main)
    RecyclerView rv;

    // static methods
    public static Intent getIntent(Context context) {
        return new Intent(context, ApiMainActivity.class);
    }

    // methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api_main);

        ButterKnife.bind(this);

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

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        rv.setAdapter(new OptionsAdapter());
    }

    // inner classes
    class OptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // constants
        private final String[] ARR = new String[] {
            "Map settings", "Venues"};

        // variables
        private LayoutInflater mInflater;

        // constructors
        public OptionsAdapter() {
            mInflater = LayoutInflater.from(ApiMainActivity.this);
        }

        // methods
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(mInflater.inflate(R.layout.item_api, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder) holder).tv.setText(ARR[position]);
        }

        @Override
        public int getItemCount() {
            return ARR.length;
        }

        class Holder extends RecyclerView.ViewHolder {

            // UI variables
            @Bind(R.id.tv_item_api)
            TextView tv;

            // constructors
            public Holder(final View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);

                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        final Context context = itemView.getContext();

                        switch (position) {
                            case 0: {
                                context.startActivity(ApiMapSettingsActivity.getIntent(context));

                                break;
                            }
                            case 1: {
                                context.startActivity(ApiVenuesActivity.getIntent(context));

                                break;
                            }
                        }
                    }
                });
            }


        }
    }
}
