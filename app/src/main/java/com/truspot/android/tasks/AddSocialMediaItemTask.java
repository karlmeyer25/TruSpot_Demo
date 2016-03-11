package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.SocialMediaItem;
import com.truspot.backend.api.model.Venue;

import java.io.IOException;

public class AddSocialMediaItemTask extends SimpleTask<Void, SocialMediaItem> {

    // variables
    private Long venueId;
    private SocialMediaItem item;

    // constructor
    public AddSocialMediaItemTask(SimpleCallback<SocialMediaItem> callback, Long venueId, SocialMediaItem item) {
        super(callback);

        this.venueId = venueId;
        this.item = item;
    }

    // methods
    @Override
    protected SocialMediaItem doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().createSocialMediaItem(venueId, item).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
