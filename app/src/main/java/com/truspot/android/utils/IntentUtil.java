package com.truspot.android.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

public class IntentUtil {
	
	public static Intent getSendEmailIntent(
			String email,
			String subject,
			String text,
			String chooserText) {
/*	    StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(email));
	    builder.append("?subject=" + Uri.encode(Uri.encode(subject)));
	    builder.append("&body=" + Uri.encode(Uri.encode(text)));

	    String uri = builder.toString();
	    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));*/
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setType("message/rfc822");
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		
		return Intent.createChooser(intent, chooserText);
	}

	public static Intent getShareIntent(
			String textToShare) {
		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with it.
		intent.putExtra(Intent.EXTRA_TEXT, textToShare);

		return intent;
	}

	public static Intent getShareIntent(
			String textToShare,
			String chooserText) {
		Intent intent = getShareIntent(textToShare);

		return Intent.createChooser(intent, chooserText);
	}

	public static Intent getBrowserIntent(
			String url) {
	     Intent intent = new Intent();
	     intent.setAction(Intent.ACTION_VIEW);
	     intent.addCategory(Intent.CATEGORY_BROWSABLE);
	     intent.setData(Uri.parse(url));

	     return intent;
	}
	
	public static Intent getMapIntent(
			double lat,
			double lng) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(String.format("geo:%s,%s", lat, lng)));
		
		return intent;
	}

    public static Intent getCallIntent(
            String phone) {
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
    }

    public static Intent getDialIntent(
            String phone) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
    }

    public static Intent getSmsIntent(
            String phone,
            String body) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phone);
        smsIntent.putExtra("sms_body", body);

        return smsIntent;
    }

    public static Intent getLocationSettingsIntent() {
        return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

    public static Intent getSettingsIntent() {
        return
                new Intent(Settings.ACTION_SETTINGS);
    }

    public static Intent getWirelessNetworkSettings() {
        return new Intent(
                Settings.ACTION_AIRPLANE_MODE_SETTINGS);
    }

    /**
     *
     * @param videoQuality < 0 ignores it, 0 low quality, 1 high quality
     * @param durationInSeconds <= 0 ignores it
     * @param sizeLimit size in bytes, <= 0 ignores it
     * */
    public static Intent getVideoCaptureIntent(
            int videoQuality,
            int durationInSeconds,
            long sizeLimit) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoQuality >= 0) {
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, videoQuality);
        }

        if (durationInSeconds > 0) {
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationInSeconds);
        }

        if (sizeLimit > 0) {
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit);
        }

        return intent;
    }

    /**
     * @param type For example "video/*" or "image/*" or "video/*, image/*"
     * */
    public static Intent getPickIntent(String type) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(type);

        return photoPickerIntent;
    }
}