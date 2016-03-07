package com.truspot.android.constants;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class Constants {
    public static final boolean IS_DEBUG = true;

    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    public static final int DEFAULT_CAMERA_ZOOM = 12;

    // google API key
    public static final String API_KEY = "AIzaSyAnxc7anZbTNM4Rbv-x1sLKE3yrWrmxgqo";

    public static final int YOUTUBE_ID_LENGTH = 11;
}
