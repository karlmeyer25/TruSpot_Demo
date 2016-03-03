package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.enums.SocialMediaEnum;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddSocialItemActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = AddSocialItemActivity.class.getName();

    public static final String BUNDLE_SOCIAL_MEDIA_TYPE = "social_media_type";

    // variables
    private SocialMediaEnum mSocialMediaType;
    private Picasso mPicasso;

    // UI
    @Bind(R.id.toolbar_activity_add_social_item)
    Toolbar toolbar;
    @Bind(R.id.fab_activity_add_social_item_share)
    FloatingActionButton fabShare;
    @Bind(R.id.iv_activity_add_social_item_container)
    ImageView ivContainer;
    @Bind(R.id.iv_activity_add_social_item_play_video)
    ImageView ivPlayVideo;

    // get intent methods
    public static Intent getIntent(Context context, SocialMediaEnum socialMediaType) {
        Intent i = new Intent(context, AddSocialItemActivity.class);
        i.putExtra(BUNDLE_SOCIAL_MEDIA_TYPE, socialMediaType);

        return i;
    }

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_social_item);

        ButterKnife.bind(this);

        initExtras();
        initVariables();
        initListeners();
        setToolbarUiSettings();
        loadSocialMedia();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void initExtras() {
        mSocialMediaType = (SocialMediaEnum) getIntent().getSerializableExtra(BUNDLE_SOCIAL_MEDIA_TYPE);
    }

    private void initVariables() {
        mPicasso = Picasso.with(this);
    }

    private void initListeners() {
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadSocialMedia() {
        mPicasso.load("https://campuscrush101.files.wordpress.com/2011/03/girly-drinks-pink-drink.jpg")
                .fit()
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(ivContainer);
    }
}
