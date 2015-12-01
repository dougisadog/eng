package com.shuangge.english.support.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;

public class WebBrowserSelector {

	public static void openLink(Context context, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		context.startActivity(intent);
	}
}