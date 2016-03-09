package com.truspot.android.constants;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class Constants {

    // debug
    public static final boolean IS_DEBUG = true;

    // camera
    public static final int DEFAULT_CAMERA_ZOOM = 12;

    // google API key
    public static final String API_KEY = "AIzaSyAnxc7anZbTNM4Rbv-x1sLKE3yrWrmxgqo";

    // context menus
    public static final int MENU_EDIT = 1;
    public static final int MENU_DELETE = 2;

    // other
    public static final int YOUTUBE_ID_LENGTH = 11;
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();
}
