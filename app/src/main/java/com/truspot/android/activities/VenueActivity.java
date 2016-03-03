package com.truspot.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.truspot.android.utils.GoogleUtil;
import com.truspot.backend.api.model.VenueFull;

import java.io.IOException;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class VenueActivity extends AppCompatActivity {
    // constants
    public static final String BASIC_TAG = VenueActivity.class.getName();

    private static final String BUNDLE_VF = "vf";

    // variables
    private VenueFull mVf;

    // static methods
    public static Intent getIntent(Context context, VenueFull vf) throws IOException {
        Intent intent = new Intent(context, VenueActivity.class);

        intent.putExtra(BUNDLE_VF, GoogleUtil.objectToJsonString(vf));

        return intent;
    }

    // methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initExtras();

        // TODO add func
    }

    private void initExtras() {
        //final String TAG = Util.stringsToPath(BASIC_TAG, "initExtas");

        try {
            mVf = GoogleUtil.jsonToObject(VenueFull.class, getIntent().getStringExtra(BUNDLE_VF));

            //LogUtil.log(TAG, mVf.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
