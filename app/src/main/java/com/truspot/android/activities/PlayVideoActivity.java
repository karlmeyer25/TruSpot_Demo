package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.truspot.android.R;
import com.truspot.android.activities.abstracts.YouTubeFailureRecoveryActivity;
import com.truspot.android.constants.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayVideoActivity extends YouTubeFailureRecoveryActivity {

    // constants
    public static final String BASIC_TAG = PlayVideoActivity.class.getName();

    public static final String BUNDLE_YOUTUBE_URL = "youtube_url";

    // variables
    private String mYoutubeUrl;
    private YouTubePlayer mPlayer;

    // UI
    @Bind(R.id.toolbar_activity_play_video)
    Toolbar toolbar;
    @Bind(R.id.player_activity_play_video)
    YouTubePlayerView playerView;

    // get intent methods
    public static Intent getIntent(Context context, String youtubeUrl) {
        Intent i = new Intent(context, PlayVideoActivity.class);
        i.putExtra(BUNDLE_YOUTUBE_URL, youtubeUrl);

        return i;
    }

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        ButterKnife.bind(this);

        initExtras();
        initListeners();
        setToolbarUiSettings();
        playerView.initialize(Constants.API_KEY, this);
    }


    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer,
                                        boolean b) {

        mPlayer = youTubePlayer;

        if (!b) {
            mPlayer.loadVideo(mYoutubeUrl);
        }

    }

    private void initExtras() {
        mYoutubeUrl = getIntent().getStringExtra(BUNDLE_YOUTUBE_URL);
    }

    private void initListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setToolbarUiSettings() {
        //toolbar.setNavigationIcon(getBackArrowDrawable());
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
}
