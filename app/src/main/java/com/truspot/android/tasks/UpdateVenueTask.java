package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.Api;
import com.truspot.backend.api.model.Venue;

import java.io.IOException;

public class UpdateVenueTask extends SimpleTask<Void, Venue> {

    // variables
    private Long id;
    private Venue venue;

    // constructor
    public UpdateVenueTask(SimpleCallback<Venue> callback, Long id, Venue venue) {
        super(callback);

        this.id = id;
        this.venue = venue;
    }

    // methods
    @Override
    protected Venue doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().updateVenue(id, venue).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
