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
import com.truspot.android.tasks.AddSocialMediaItemTask;
import com.truspot.android.tasks.AddVenueTask;
import com.truspot.android.tasks.UpdateSocialMediaItemTask;
import com.truspot.android.tasks.UpdateVenueTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.backend.api.model.SocialMediaItem;
import com.truspot.backend.api.model.Venue;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditSocialMediaItemActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = EditSocialMediaItemActivity.class.getName();

    private static final String BUNDLE_IS_ADD = "is_add";
    private static final String BUNDLE_VENUE_ID = "venue_id";
    private static final String BUNDLE_SOCIAL_MEDIA_ITEM = "social_media_item";

    // variables
    private boolean mIsAdd;
    private Long mVenueId;
    private SocialMediaItem mSocialMediaItem;

    // UI
    @Bind(R.id.toolbar_activity_edit_social_media_item)
    Toolbar toolbar;
    @Bind(R.id.et_activity_edit_social_media_item_username)
    EditText etUsername;
    @Bind(R.id.et_activity_edit_social_media_item_avatar_url)
    EditText etAvatarUrl;
    @Bind(R.id.et_activity_edit_social_media_item_text)
    EditText etText;
    @Bind(R.id.et_activity_edit_social_media_item_photo_url)
    EditText etPhotoUrl;
    @Bind(R.id.et_activity_edit_social_media_item_video_url)
    EditText etVideoUrl;
    @Bind(R.id.btn_activity_edit_social_media_item_update)
    Button btnUpdate;

    // get intent methods
    public static Intent getIntent(Context context,
                                   boolean isAdd,
                                   Long venueId,
                                   SocialMediaItem socialMediaItem) throws IOException {
        Intent i = new Intent(context, EditSocialMediaItemActivity.class);
        i.putExtra(BUNDLE_IS_ADD, isAdd);
        i.putExtra(BUNDLE_VENUE_ID, venueId);
        i.putExtra(BUNDLE_SOCIAL_MEDIA_ITEM, GoogleUtil.objectToJsonString(socialMediaItem));

        return i;
    }

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_social_media_item);

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
        mVenueId = getIntent().getLongExtra(BUNDLE_VENUE_ID, 0);

        try {
            mSocialMediaItem = mIsAdd ?
                    null :
                    GoogleUtil.jsonToObject(SocialMediaItem.class, getIntent().getStringExtra(BUNDLE_SOCIAL_MEDIA_ITEM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        if (mSocialMediaItem == null) {
            mSocialMediaItem = new SocialMediaItem();
        }
    }

    private void initListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocialMediaItem.setUsername(etUsername.getText().toString());
                mSocialMediaItem.setAvatarUrl(etAvatarUrl.getText().toString());
                mSocialMediaItem.setText(etText.getText().toString());
                mSocialMediaItem.setPhotoUrl(etPhotoUrl.getText().toString());
                mSocialMediaItem.setVideoUrl(etVideoUrl.getText().toString());

                if (mSocialMediaItem.getId() != null && mSocialMediaItem.getId() != 0) {
                    new UpdateSocialMediaItemTask(new SimpleTask.SimpleCallback<SocialMediaItem>() {
                        @Override
                        public void onStart() {
                            // do nothing
                        }

                        @Override
                        public void onComplete(SocialMediaItem res) {
                            LogUtil.log(BASIC_TAG, "updated!");
                            setResult(RESULT_OK);
                            finish();
                        }

                    }, mVenueId, mSocialMediaItem.getId(), mSocialMediaItem).execute();
                } else {
                    new AddSocialMediaItemTask(new SimpleTask.SimpleCallback<SocialMediaItem>() {
                        @Override
                        public void onStart() {
                            // do nothing
                        }

                        @Override
                        public void onComplete(SocialMediaItem res) {
                            LogUtil.log(BASIC_TAG, "added!");
                            setResult(RESULT_OK);
                            finish();
                        }

                    }, mVenueId, mSocialMediaItem).execute();
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
        if (mSocialMediaItem != null) {
            etUsername.setText(mSocialMediaItem.getUsername() != null ? mSocialMediaItem.getUsername() : "");
            etAvatarUrl.setText(mSocialMediaItem.getAvatarUrl() != null ? mSocialMediaItem.getAvatarUrl() : "");
            etText.setText(mSocialMediaItem.getText() != null ? mSocialMediaItem.getText() : "");
            etPhotoUrl.setText(mSocialMediaItem.getPhotoUrl() != null ? mSocialMediaItem.getPhotoUrl() : "");
            etVideoUrl.setText(mSocialMediaItem.getVideoUrl() != null ? mSocialMediaItem.getVideoUrl() : "");
        }
    }
}
