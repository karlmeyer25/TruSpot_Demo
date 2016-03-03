package com.truspot.android.api;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.truspot.android.constants.Constants;
import com.truspot.backend.api.Api;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class RemoteApi {

    private static Api mApi;

    public static Api getInstance() {
        if (mApi == null) {
            mApi = new Api.Builder(AndroidHttp.newCompatibleTransport(), Constants.JSON_FACTORY, null).
                    setRootUrl("https://truspot-android.appspot.com/_ah/api").
                    build();

/*            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            })*/
        }

        return mApi;
    }
}
