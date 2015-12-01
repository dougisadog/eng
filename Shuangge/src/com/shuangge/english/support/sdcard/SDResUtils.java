package com.shuangge.english.support.sdcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

public class SDResUtils {

	// 文件至今的时间 单位s 
	private static long LAST_TIME = 2 * 60; //2分钟

	@SuppressLint({ "InlinedApi", "NewApi" })
	private static String getRecentlyPhotoId(Context context) {
		Long lastTime = System.currentTimeMillis() / 1000 - LAST_TIME;
		String searchPath = MediaStore.Files.FileColumns.DATA + " LIKE '%.jpg' or "
						  + MediaStore.Files.FileColumns.DATA + " LIKE '%.gif' and "
				          + MediaStore.Files.FileColumns.DATE_ADDED + " > " + lastTime.toString();

		Uri uri = MediaStore.Files.getContentUri("external");
		// String[] selections = new String[]{MediaStore.Files.FileColumns._ID ,
		// MediaStore.Files.FileColumns.DATE_ADDED};
		String[] selections = new String[] { MediaStore.Files.FileColumns._ID };
		Cursor cursor = context.getContentResolver().query(uri, selections,searchPath, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
		String filePath = "";
		// 降序排列 取第一张 为 最新的照片
		if (cursor != null && cursor.moveToFirst()) {
			// 看这里我们取id了
			filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
			// String time = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED));
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return filePath;
	}

	public static Bitmap getCurrentPhoto(Context context) {
		Bitmap bitmap = null;
		if (SDUtils.checkSDCard()) {
			String photoId = SDResUtils.getRecentlyPhotoId(context);
			if (!TextUtils.isEmpty(photoId)) {
				bitmap = MediaStore.Images.Thumbnails.getThumbnail(
						context.getContentResolver(), Long.parseLong(photoId),
						MediaStore.Images.Thumbnails.MINI_KIND,
						new BitmapFactory.Options());
				
//		MediaStore.Images.Thumbnails.MINI_KIND; 512 x 384 
//		MediaStore.Images.Thumbnails.MICRO_KIND; 96 x 96
 			}
		}
		return bitmap;
	}

}
