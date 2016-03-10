package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.SocialMediaItem;

import java.io.IOException;

public class DeleteSocialMediaItemTask extends SimpleTask<Void, SocialMediaItem> {

    // variables
    private Long venueId;
    private Long id;

    // constructor
    public DeleteSocialMediaItemTask(SimpleCallback<SocialMediaItem> callback, Long venueId, Long id) {
        super(callback);

        this.venueId = id;
        this.id = id;
    }

    // methods
    @Override
    protected SocialMediaItem doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().removeSocialMediaItem(venueId, id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
