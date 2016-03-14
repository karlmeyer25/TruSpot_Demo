package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.MapSettings;

import java.io.IOException;

/**
 * Created by yavoryordanov on 3/14/16.
 */
public class GetMapSettingsTask extends SimpleTask<Void, MapSettings> {

    // constructors
    public GetMapSettingsTask(SimpleCallback<MapSettings> callback) {
        super(callback);
    }

    // methods
    @Override
    protected MapSettings doInBackground(Void... params) {
        try {
            return RemoteApi.getInstance().getMapSettings().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
