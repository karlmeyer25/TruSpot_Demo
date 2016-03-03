package com.truspot.android.tasks;

import com.truspot.android.api.RemoteApi;
import com.truspot.android.tasks.abstracts.SimpleTask;
import com.truspot.backend.api.model.VenueFull;
import java.io.IOException;
import java.util.List;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class GetVenuesFullTask extends SimpleTask<Void, List<VenueFull>>{

    public GetVenuesFullTask(SimpleCallback<List<VenueFull>> callback) {
        super(callback);
    }

    @Override
    protected List<VenueFull> doInBackground(Void... params) {
        try {
            return RemoteApi.getInstance().getVenuesFull().execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
