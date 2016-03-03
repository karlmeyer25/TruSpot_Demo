package com.truspot.android.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.TransactionTooLargeException;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Util {

    public static final int MAX_LENGTH = 30;

    public static void hideKeyboard(Activity activity,
                                    IBinder binder) {
        InputMethodManager imm;

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(binder, 0);
    }

    public static void showNotification(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongNotification(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showSafeNotification(Fragment fragment, String msg) {
        if (isActivityAlive(fragment)) {
            showNotification(fragment.getActivity(), msg);
        }
    }

    public static void showSafeLongNotification(Fragment fragment, String msg) {
        if (isActivityAlive(fragment)) {
            showLongNotification(fragment.getActivity(), msg);
        }
    }

    public static String getCurrLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public static boolean isActivityAlive(Fragment fragment) {
        return !fragment.isDetached() && fragment.getActivity() != null;
    }

    public static boolean isActivityAlive(android.support.v4.app.Fragment fragment) {
        return !fragment.isDetached() && fragment.getActivity() != null;
    }

    public static boolean isFragmentOk(Fragment fragment) {
        return fragment != null && isActivityAlive(fragment);
    }

    public static boolean isFragmentOk(android.support.v4.app.Fragment fragment) {
        return fragment != null && isActivityAlive(fragment);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String stringsToPath(String... strs) {
        String res = "";

        for (int i = 0; i < strs.length; i++) {
            res += strs[i] + "/";
        }

        return res.length() > 0 ? res.substring(0, res.length() - 1) : res;
    }

    public static boolean isListNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isStringNotNull(String str) {
        return str != null && !str.equals("");
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();

        return s;
    }

    public static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf('.');

        if (lastIndex > -1) {
            return filePath.substring(lastIndex + 1, filePath.length());
        } else {
            return null;
        }
    }

    public static Point getScreenDimensions(Activity activity) {
        Point point = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(point);

        return point;
    }

    public static int getActivityDegrees(Activity activity) {
        int activityRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int activityDegrees = 0;

        switch (activityRotation) {
            case Surface.ROTATION_0: activityDegrees = 0; break;
            case Surface.ROTATION_90: activityDegrees = 90; break;
            case Surface.ROTATION_180: activityDegrees = 180; break;
            case Surface.ROTATION_270: activityDegrees = 270; break;
        }

        return activityDegrees;
    }

    /**
     * @param value A value between 0 and 255
     * */
    public static void setToolbarBgAlpha(Toolbar toolbar, int value) {
        ColorDrawable bg = (ColorDrawable) toolbar.getBackground();

        // fix issue with Lollipop
        if(bg.getAlpha() != value) {
            bg.setAlpha(value);
        }
    }

    public static int getToolbarBgAlpha(Toolbar toolbar) {
        return ((ColorDrawable) toolbar.getBackground()).getAlpha();
    }

    /**
     * Converts an InputStream to a String.
     **/
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static boolean isPortraitOrientation(Resources res) {
        return  res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscapeOrientation(Resources res) {
        return  res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String joinLong(String separator, long... args) {
        StringBuilder builder = new StringBuilder();

        if (args != null && args.length > 0) {
            for (int i = 0, z = args.length; i < z; i++) {
                if (i > 0) {
                    builder.append(separator);
                }

                builder.append(String.valueOf(args[i]));
            }
        }

        return builder.toString();
    }

    public static int convertDpiToPixels(Context context, double dpi) {
        int result;
        final float density;

        density = context.getResources().getDisplayMetrics().density;
        result = (int) ((dpi * density) + 0.5f);

        return result;
    }

    public static long[] splitLong(String separator, String str) {
        if (isStringNotNull(str)) {
            String[] splitted = str.split(separator);
            long[] result = new long[splitted.length];

            for (int i = 0, z = splitted.length; i < z; i++) {
                result[i] = Long.valueOf(splitted[i]);
            }

            return result;
        }

        return null;
    }

    public static String getAddressString(Address address, String separator) {
        ArrayList<String> addressFragments = new ArrayList<String>();

        // Fetch the address lines using getAddressLine,
        // join them, and send them to the thread.
        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }

        return TextUtils.join(separator, addressFragments);
    }

    public static boolean hasConnection (Context context) {
        ConnectivityManager man;

        man = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = man.getActiveNetworkInfo();

        return netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    public static String generateRandomPassword() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static <T> T[] addElementsToArray(Class<T> clazz, T[] array, T... newElements) {
        T[] newArray =  (T[]) Array.newInstance(clazz, array.length + newElements.length);

        for (int i = 0, z = array.length; i < z; i++) {
            newArray[i] = array[i];
        }

        for (int begin = array.length, i = array.length, z = newArray.length; i < z; i++) {
            newArray[i] = newElements[i - begin];
        }

        return newArray;
    }

    /**
     * NB: To be called only in thread!
     * */
    public static List<ApplicationInfo> getInstalledApps(
            Context context,
            boolean excludeCurrent,
            boolean sort) throws TransactionTooLargeException {
        PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> result = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        if (excludeCurrent) {
            result.remove(context.getPackageName());
        }

        if (sort) {
            Collections.sort(result, new ApplicationInfo.DisplayNameComparator(pm));
        }

        return result;
    }

    /**
     * NB: To be called only in thread!
     * */
    public static List<String> getInstalledPackages(
            Context context,
            boolean excludeCurrent,
            boolean sort) throws TransactionTooLargeException {
        List<ApplicationInfo> apps = getInstalledApps(context, excludeCurrent, sort);

        if (Util.isListNotEmpty(apps)) {
            return appsToPackages(apps);
        }

        return null;
    }

    public static List<String> appsToPackages(List<ApplicationInfo> apps) {
        List<String> packages = new ArrayList<>();

        for (ApplicationInfo ai : apps) {
            packages.add(ai.packageName);
        }

        return packages;
    }

    public static void setLocale(Context context, Locale locale) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}