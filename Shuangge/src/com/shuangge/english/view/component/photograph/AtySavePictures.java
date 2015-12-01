package com.shuangge.english.view.component.photograph;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class AtySavePictures extends Activity implements OnClickListener {
	
	public static ImageView iv;

	public static final String CALLBACK_DATAS_BYTES = "callback datas bytes";

	public static final int CODE_CANCEL = 0;
	public static final int CODE_BITMAP = 1;
	public static final int CODE_DELETE = 2;

	private static final int NONE = 0;
	private static final int PHOTO_GRAPH = 1;// 拍照
	private static final int PHOTO_ZOOM = 2; // 缩放
	private static final int PHOTO_RESOULT = 3;// 结果
	private static final String IMAGE_UNSPECIFIED = "image/*";

	private FrameLayout flBg;
	private FrameLayout flSavePics = null, flCancel = null;

	private String path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_save_pictures);

		flSavePics = (FrameLayout) findViewById(R.id.flSavePics);
		flSavePics.setOnClickListener(this);
		flCancel = (FrameLayout) findViewById(R.id.flCancel);
		flCancel.setOnClickListener(this);

		flBg = (FrameLayout) findViewById(R.id.flBg1);
		flBg.setOnClickListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flBg1:
			finish();
			break;
		case R.id.flSavePics:
			String url = MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable) iv.getDrawable()).getBitmap(), "", ""); 
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + getFilePathByContentResolver(this, Uri.parse(url)))));
			Toast.makeText(this, R.string.savePicsTip, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case R.id.flCancel:
			finish();
			break;
		}
	}

	private String getFilePathByContentResolver(Context context, Uri uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null,
				null);
		String filePath = null;
		if (null == c) {
			throw new IllegalArgumentException("Query on " + uri
					+ " returns null result.");
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} 
			else {
				filePath = c.getString(c.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} 
		finally {
			c.close();
		}
		return filePath;
	}
	
	public static void startAty(Context context, ImageView iv) {
		AtySavePictures.iv = iv;
		context.startActivity(new Intent(context, AtySavePictures.class));
	}
}