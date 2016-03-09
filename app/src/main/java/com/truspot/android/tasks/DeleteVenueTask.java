package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.Venue;

import java.io.IOException;

public class DeleteVenueTask extends SimpleTask<Void, Venue> {

    // variables
    private Long id;

    // constructor
    public DeleteVenueTask(SimpleCallback<Venue> callback, Long id) {
        super(callback);

        this.id = id;
    }

    // methods
    @Override
    protected Venue doInBackground(Void... params) {
        try {
            RemoteApi.getInstance().removeVenue(id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
