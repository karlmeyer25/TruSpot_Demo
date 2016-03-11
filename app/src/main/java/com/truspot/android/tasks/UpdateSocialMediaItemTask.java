package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.SocialMediaItem;

import java.io.IOException;

public class UpdateSocialMediaItemTask extends SimpleTask<Void, SocialMediaItem> {

    // variables
    private Long venueId;
    private Long id;
    private SocialMediaItem item;

    // constructor
    public UpdateSocialMediaItemTask(SimpleCallback<SocialMediaItem> callback,
                                     Long venueId,
                                     Long id,
                                     SocialMediaItem item) {
        super(callback);

        this.venueId = venueId;
        this.id = id;
        this.item = item;
    }

    // methods
    @Override
    protected SocialMediaItem doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().updateSocialMediaItem(venueId, id, item).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}