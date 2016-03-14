package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.truspot.android.R;
import com.truspot.android.tasks.GetMapSettingsTask;
import com.truspot.android.tasks.UpdateMapSettingsTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.MapSettings;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yavoryordanov on 3/14/16.
 */
public class ApiMapSettingsActivity extends AppCompatActivity{
    // constants
    public static final String BASIC_TAG = ApiMapSettingsActivity.class.getName();

    // variables

    // UI
    @Bind(R.id.toolbar_activity_api_map_settings)
    Toolbar toolbar;
    @Bind(R.id.et_activity_api_map_settings_lat)
    EditText etLat;
    @Bind(R.id.et_activity_api_map_settings_lng)
    EditText etLng;
    @Bind(R.id.et_activity_api_map_settings_zoom)
    EditText etZoom;
    @Bind(R.id.btn_activity_api_map_settings_update)
    Button btnUpdate;


    // static methods
    public static Intent getIntent(Context context) {
        return new Intent(context, ApiMapSettingsActivity.class);
    }

    // methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api_map_settings);

        ButterKnife.bind(this);

        setToolbarUiSettings();
        initListeners();

        if (Util.hasConnection(this)) {
            new GetMapSettingsTask(new SimpleTask.SimpleCallback<MapSettings>() {

                @Override
                public void onStart() {
                    enableFields(false);
                }

                @Override
                public void onComplete(MapSettings res) {
                    if (res != null) {
                        etLat.setText(String.valueOf(res.getLat()));
                        etLng.setText(String.valueOf(res.getLng()));
                        etZoom.setText(String.valueOf(res.getZoom()));
                    }

                    enableFields(true);
                }

            }).execute();

        } else {
            Util.showLongNotification(this, getString(R.string.toast_no_internet_connection));

            finish();
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

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enableFields(boolean enable) {
        etLat.setEnabled(enable);
        etLng.setEnabled(enable);
        etZoom.setEnabled(enable);
        btnUpdate.setEnabled(enable);
    }

    private void initListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.hasConnection(ApiMapSettingsActivity.this)) {
                    MapSettings ms = new MapSettings();
                    ms.setLat(Double.valueOf(etLat.getText().toString()));
                    ms.setLng(Double.valueOf(etLng.getText().toString()));
                    ms.setZoom(Integer.valueOf(etZoom.getText().toString()));

                    new UpdateMapSettingsTask(ms, new SimpleTask.SimpleCallback<MapSettings>() {

                        @Override
                        public void onStart() {
                            enableFields(false);
                        }

                        @Override
                        public void onComplete(MapSettings res) {
                            enableFields(true);

                            if (res != null) {
                                Util.showNotification(
                                        ApiMapSettingsActivity.this,
                                        getString(R.string.toast_map_settings_updated));

                                finish();
                            } else {
                                Util.showLongNotification(
                                        ApiMapSettingsActivity.this,
                                        getString(R.string.error_cant_update_map_settings));
                            }
                        }

                    }).execute();
                } else {
                    Util.showLongNotification(ApiMapSettingsActivity.this, getString(R.string.toast_no_internet_connection));
                }
            }

        });
    }
}
