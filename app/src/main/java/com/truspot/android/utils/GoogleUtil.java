package com.truspot.android.utils;

import com.truspot.android.constants.Constants;

import java.io.IOException;

/**
 * Created by yavoryordanov on 2/12/15.
 */
public class GoogleUtil {

    public static String objectToJsonString(Object obj)
            throws IOException {
        return Constants.JSON_FACTORY.toString(obj);
    }

    public static <O> O jsonToObject(Class<O> cls, String str)
            throws IOException {
        return Constants.JSON_FACTORY.fromString(str, cls);
    }
}
