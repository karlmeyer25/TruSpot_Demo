package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.truspot.android.R;
import com.truspot.android.tasks.AddVenueTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.tasks.UpdateVenueTask;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.backend.api.model.Venue;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditVenueActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = EditVenueActivity.class.getName();

    private static final String BUNDLE_IS_ADD = "is_add";
    private static final String BUNDLE_VF = "vf";

    private static final int REQUEST_SOCIAL_MEDIA = 1;

    // variables
    private boolean mIsAdd;
    private VenueFull mVf;
    private Venue mVenue;

    // UI
    @Bind(R.id.toolbar_activity_edit_venue)
    Toolbar toolbar;
    @Bind(R.id.et_activity_edit_venue_name)
    EditText etName;
    @Bind(R.id.et_activity_edit_venue_description)
    EditText etDescription;
    @Bind(R.id.et_activity_edit_venue_lat)
    EditText etLat;
    @Bind(R.id.et_activity_edit_venue_lng)
    EditText etLng;
    @Bind(R.id.et_activity_edit_venue_capacity)
    EditText etCapacity;
    @Bind(R.id.et_activity_edit_venue_occupancy)
    EditText etOccupancy;
    @Bind(R.id.et_activity_edit_venue_pdm_color)
    EditText etPdmColor;
    @Bind(R.id.btn_activity_edit_venue_view_social_media)
    Button btnViewSocialMedia;
    @Bind(R.id.btn_activity_edit_venue_update)
    Button btnUpdate;

    // get intent methods
    public static Intent getIntent(Context context, boolean isAdd, VenueFull vf) throws IOException {
        Intent i = new Intent(context, EditVenueActivity.class);
        i.putExtra(BUNDLE_IS_ADD, isAdd);
        i.putExtra(BUNDLE_VF, GoogleUtil.objectToJsonString(vf));

        return i;
    }

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_venue);

        ButterKnife.bind(this);

        initExtras();
        initVariables();
        initListeners();
        setToolbarUiSettings();
        setupUi();
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

    private void initExtras() {
        mIsAdd = getIntent().getBooleanExtra(BUNDLE_IS_ADD, false);

        try {
            mVf = mIsAdd ?
                    null :
                    GoogleUtil.jsonToObject(VenueFull.class, getIntent().getStringExtra(BUNDLE_VF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        mVenue = mVf != null ? mVf.getVenue() : new Venue();
    }

    private void initListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVenue.setName(etName.getText().toString());
                mVenue.setDescription(etDescription.getText().toString());
                mVenue.setLat(Double.valueOf(etLat.getText().toString()));
                mVenue.setLng(Double.valueOf(etLng.getText().toString()));
                mVenue.setCapacity(Integer.valueOf(etCapacity.getText().toString()));
                mVenue.setOccupancy(Integer.valueOf(etOccupancy.getText().toString()));
                mVenue.setPdmColor(etPdmColor.getText().toString());

                if (mVf != null) {
                    new UpdateVenueTask(new SimpleTask.SimpleCallback<Venue>() {
                        @Override
                        public void onStart() {
                            // do nothing
                        }

                        @Override
                        public void onComplete(Venue res) {
                            setResult(RESULT_OK);
                            finish();
                        }

                    }, mVf.getVenue().getId(), mVenue).execute();
                } else {
                    new AddVenueTask(new SimpleTask.SimpleCallback<Venue>() {
                        @Override
                        public void onStart() {
                            // do nothing
                        }

                        @Override
                        public void onComplete(Venue res) {
                            setResult(RESULT_OK);
                            finish();
                        }

                    }, mVenue).execute();
                }
            }
        });

        btnViewSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVf != null) {
                    try {
                        startActivityForResult(SocialMediaItemsActivity.getIntent(EditVenueActivity.this, mVf), REQUEST_SOCIAL_MEDIA);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(EditVenueActivity.this,
                            "First create a venue and then you will add/edit/delete social media items!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupUi() {
        if (mVf != null) {
            etName.setText(mVf.getVenue().getName());
            etDescription.setText(mVf.getVenue().getDescription());
            etLat.setText(String.valueOf(mVf.getVenue().getLat()));
            etLng.setText(String.valueOf(mVf.getVenue().getLng()));
            etCapacity.setText(String.valueOf(mVf.getVenue().getCapacity()));
            etOccupancy.setText(String.valueOf(mVf.getVenue().getOccupancy()));
            etPdmColor.setText(mVf.getVenue().getPdmColor());
        }

        btnViewSocialMedia.setText(String.format("View social media (%s)", (mVf != null && mVf.getFeed() != null) ?
                mVf.getFeed().size() : 0));
    }

}
