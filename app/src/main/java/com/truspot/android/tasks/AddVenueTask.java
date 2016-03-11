package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.Venue;

import java.io.IOException;

public class AddVenueTask extends SimpleTask<Void, Venue> {

    // variables
    private Venue venue;

    // constructor
    public AddVenueTask(SimpleCallback<Venue> callback, Venue venue) {
        super(callback);

        this.venue = venue;
    }

    // methods
    @Override
    protected Venue doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().createVenue(venue).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}