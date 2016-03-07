package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import com.truspot.android.picasso.RoundedTransformation;
import com.truspot.android.utils.Util;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddSocialItemActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = AddSocialItemActivity.class.getName();

    public static final String BUNDLE_SOCIAL_MEDIA_TYPE = "social_media_type";
    public static final String BUNDLE_SOCIAL_MEDIA_PATH = "social_media_path";

    // variables
    private SocialMediaEnum mSocialMediaType;
    private String mSocialMediaPath;
    private Picasso mPicasso;

    // UI
    @Bind(R.id.toolbar_activity_add_social_item)
    Toolbar toolbar;
    @Bind(R.id.fab_activity_add_social_item_share)
    FloatingActionButton fabShare;
    @Bind(R.id.iv_activity_add_social_item_avatar)
    ImageView ivAvatar;
    @Bind(R.id.iv_activity_add_social_item_container)
    ImageView ivContainer;
    @Bind(R.id.iv_activity_add_social_item_video)
    ImageView ivVideo;

    // get intent methods
    public static Intent getIntent(Context context,
                                   SocialMediaEnum socialMediaType,
                                   String socialMediaPath) {
        Intent i = new Intent(context, AddSocialItemActivity.class);
        i.putExtra(BUNDLE_SOCIAL_MEDIA_TYPE, socialMediaType);
        i.putExtra(BUNDLE_SOCIAL_MEDIA_PATH, socialMediaPath);

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
        setUiSettings();
        loadUserAvatar();
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
        mSocialMediaPath = getIntent().getStringExtra(BUNDLE_SOCIAL_MEDIA_PATH);
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

    private void setUiSettings() {
        setToolbarUiSettings();
        setMediaUiSettings();
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setMediaUiSettings() {
        ivVideo.setVisibility(mSocialMediaType == SocialMediaEnum.VIDEO ? View.VISIBLE : View.GONE);
        ivContainer.setVisibility(mSocialMediaType == SocialMediaEnum.TEXT ? View.GONE : View.VISIBLE);
    }

    private void loadUserAvatar() {
        mPicasso.load("http://greatist.com/sites/default/files/styles/square_profile/public/Jeff-Cattel-Headshot1.jpg")
                .placeholder(R.drawable.default_avatar)
                .noFade()
                .resize(
                        Util.convertDpiToPixels(this, 100),
                        Util.convertDpiToPixels(this, 100))
                .centerCrop()
                .transform(new RoundedTransformation(
                        Util.convertDpiToPixels(this, 100) / 2,
                        0))
                .config(Bitmap.Config.RGB_565)
                .into(ivAvatar);
    }

    private void loadSocialMedia() {
        if (mSocialMediaType == SocialMediaEnum.PHOTO) {
            mPicasso.load(new File(mSocialMediaPath))
                    .fit()
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)
                    .into(ivContainer);
        } else if (mSocialMediaType == SocialMediaEnum.VIDEO) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(mSocialMediaPath);
            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000);
            ivContainer.setImageBitmap(bmFrame);
        }
    }
}
