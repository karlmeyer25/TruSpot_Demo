package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.truspot.android.R;
import com.truspot.android.enums.SocialMediaEnum;
import com.truspot.android.utils.IntentUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocialItemActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = SocialItemActivity.class.getName();

    private static final int REQUEST_ADD_SOCIAL_ITEM = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    // UI
    @Bind(R.id.toolbar_activity_social_item)
    Toolbar toolbar;
    @Bind(R.id.ftl_activity_social_item)
    FABToolbarLayout ftl;
    @Bind(R.id.fab_activity_social_item)
    FloatingActionButton fab;
    @Bind(R.id.iv_activity_social_item_add_text)
    ImageView ivAddText;
    @Bind(R.id.iv_activity_social_item_add_photo)
    ImageView ivAddPhoto;
    @Bind(R.id.iv_activity_social_item_add_video)
    ImageView ivAddVideo;

    // get intent methods
    public static Intent getIntent(Context context) {
        return new Intent(context, SocialItemActivity.class);
    }

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_item);

        ButterKnife.bind(this);

        initListeners();
        setToolbarUiSettings();
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

    @Override
    public void onBackPressed() {
        if (ftl.isToolbar()) {
            ftl.hide();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_SOCIAL_ITEM && resultCode == RESULT_OK) {
            Toast.makeText(SocialItemActivity.this,
                    "Successfully shared!",
                    Toast.LENGTH_LONG).show();
        }

        if (requestCode == REQUEST_PICK_IMAGE &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            String realPathFromUri = Util.getRealPathFromURI(this, uri);

            LogUtil.log(BASIC_TAG, "Real path " + realPathFromUri);

            goToAddSocialItemActivity(SocialMediaEnum.PHOTO, realPathFromUri);
        }
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftl.show();
            }
        });

        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftl.hide();

                Intent intent = IntentUtil.getPickIntent("image/*");
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void goToAddSocialItemActivity(SocialMediaEnum socialMediaEnum,
                                           String socialMediaPath) {

        Intent goToAddSocialItemActivity =
                AddSocialItemActivity.getIntent(SocialItemActivity.this,
                        socialMediaEnum, socialMediaPath);

        startActivityForResult(goToAddSocialItemActivity, REQUEST_ADD_SOCIAL_ITEM);
    }

}
