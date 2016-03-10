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
import com.truspot.android.adapters.SocialMediaAdapter;
import com.truspot.android.constants.Constants;
import com.truspot.android.tasks.DeleteSocialMediaItemTask;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.android.ui.DividerItemDecoration;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.backend.api.model.SocialMediaItem;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocialMediaItemsActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = SocialMediaItemsActivity.class.getName();

    private static final int SHOW_RV = 1;
    private static final int SHOW_PROGRESS_VIEW = 2;
    private static final int SHOW_EMPTY = 3;

    private static final int REQUEST_ADD_SOCIAL_MEDIA = 1;
    private static final int REQUEST_EDIT_SOCIAL_MEDIA = 2;

    private static final String BUNDLE_VF = "vf";

    // variables
    private VenueFull mVf;
    private SocialMediaAdapter mAdapter;

    // UI
    @Bind(R.id.toolbar_activity_social_media_items)
    Toolbar toolbar;
    @Bind(R.id.rv_activity_social_media_items)
    RecyclerView rv;
    @Bind(R.id.pv_activity_social_media_items)
    ProgressView pv;
    @Bind(R.id.tv_activity_social_media_items_empty)
    TextView tvEmpty;
    @Bind(R.id.fab_activity_social_media_items)
    FloatingActionButton fab;

    // get intent methods
    public static Intent getIntent(Context context, VenueFull vf) throws IOException {
        Intent i = new Intent(context, SocialMediaItemsActivity.class);
        i.putExtra(BUNDLE_VF, GoogleUtil.objectToJsonString(vf));

        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_items);

        ButterKnife.bind(this);

        initExtras();
        initVariables();
        initListeners();
        setToolbarUiSettings();
        loadSocialMedia();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ADD_SOCIAL_MEDIA || requestCode == REQUEST_EDIT_SOCIAL_MEDIA) &&
                resultCode == RESULT_OK) {
            Toast.makeText(SocialMediaItemsActivity.this,
                    requestCode == REQUEST_ADD_SOCIAL_MEDIA ?
                            "Social media successfully added! Restart application to see the changes!" :
                            "Social media successfully updated! Restart application to see the changes!",
                    Toast.LENGTH_LONG).show();

            loadSocialMedia();
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
                    startActivityForResult(EditSocialMediaItemActivity.getIntent(SocialMediaItemsActivity.this,
                            false, mVf.getVenue().getId(), mAdapter.getItem(mAdapter.getPosition())),
                            REQUEST_EDIT_SOCIAL_MEDIA);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.MENU_DELETE:
                new DeleteSocialMediaItemTask(new SimpleTask.SimpleCallback<SocialMediaItem>() {
                    @Override
                    public void onStart() {
                        showView(SHOW_PROGRESS_VIEW);
                    }

                    @Override
                    public void onComplete(SocialMediaItem res) {
                        mAdapter.removeItem(mAdapter.getPosition(), true);

                        if (mAdapter.getItemCount() > 0) {
                            showView(SHOW_RV);
                        } else {
                            showView(SHOW_EMPTY);
                        }
                    }

                }, mVf.getVenue().getId(),
                        mAdapter.getItem(mAdapter.getPosition()).getId()).execute();

                break;
        }

        return super.onContextItemSelected(item);
    }

    private void initExtras() {
        try {
            mVf = GoogleUtil.jsonToObject(VenueFull.class, getIntent().getStringExtra(BUNDLE_VF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        mAdapter = new SocialMediaAdapter(this);
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(EditSocialMediaItemActivity.getIntent(SocialMediaItemsActivity.this,
                            true, mVf.getVenue().getId(), null), REQUEST_ADD_SOCIAL_MEDIA);
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

    private void loadSocialMedia() {
        if (mVf != null) {
            if (mAdapter.getItemCount() > 0) {
                mAdapter.clearData(true);
            }

            if (mVf.getFeed() != null) {
                mAdapter.addData(mVf.getFeed(), true);
            }

            setupRecyclerView();
        }
    }

    private void showView(int viewId) {
        pv.setVisibility(viewId == SHOW_PROGRESS_VIEW ? View.VISIBLE : View.GONE);
        rv.setVisibility(viewId == SHOW_RV ? View.VISIBLE : View.GONE);
        tvEmpty.setVisibility(viewId == SHOW_EMPTY ? View.VISIBLE : View.GONE);
    }
}
