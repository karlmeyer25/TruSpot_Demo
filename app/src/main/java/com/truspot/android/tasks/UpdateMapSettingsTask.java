package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.MapSettings;

import java.io.IOException;

/**
 * Created by yavoryordanov on 3/14/16.
 */
public class UpdateMapSettingsTask extends SimpleTask<Void, MapSettings> {

    // variables
    private MapSettings ms;

    // constructors
    public UpdateMapSettingsTask(MapSettings mapSettings, SimpleCallback<MapSettings> callback) {
        super(callback);

        this.ms = mapSettings;
    }

    // methods
    @Override
    protected MapSettings doInBackground(Void... params) {
        try {
            return RemoteApi.getInstance().updateMapSettings(ms).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
