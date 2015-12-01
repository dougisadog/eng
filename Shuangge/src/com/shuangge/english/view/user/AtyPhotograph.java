package com.shuangge.english.view.user;

import java.io.ByteArrayOutputStream;
import java.io.File;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.utils.Utility;

public class AtyPhotograph extends Activity implements OnClickListener {
	
	public static final String NO_DELETE = "no delete";
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
	private FrameLayout flPhotograph = null, flAlbum = null, flDelete = null;
	
	private String path;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_photograph);

		flPhotograph = (FrameLayout) findViewById(R.id.flPhotograph);
		flPhotograph.setOnClickListener(this);
		flAlbum = (FrameLayout) findViewById(R.id.flAlbum);
		flAlbum.setOnClickListener(this);
		flDelete = (FrameLayout) findViewById(R.id.flDelete);
		flDelete.setOnClickListener(this);
		
		flBg = (FrameLayout) findViewById(R.id.flBg1);
		flBg.setOnClickListener(this);
		
		flDelete.setVisibility(getIntent().getBooleanExtra(NO_DELETE, false) ? View.GONE : View.VISIBLE);
	}
	
	@Override
	public void onConfigurationChanged(Configuration config) { 
		super.onConfigurationChanged(config); 
	} 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flAlbum:
			Intent i = new Intent(Intent.ACTION_PICK, null);
			i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
			startActivityForResult(i, PHOTO_ZOOM);
			break;
		case R.id.flPhotograph:
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtils.createNewTempFileByUrl("temp.jpg")));
				startActivityForResult(intent, PHOTO_GRAPH);
				
//				Intent intentFromGallery = new Intent();
//                intentFromGallery.setType(IMAGE_UNSPECIFIED); 
//                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intentFromGallery, PHOTO_GRAPH);
			}
			catch (Exception e) {
				Toast.makeText(AtyPhotograph.this, "拍照异常" + e, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.flDelete:
			callback(CODE_DELETE);
			break;
		default:
			callback(CODE_CANCEL);
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTO_GRAPH) {
			// 设置文件保存路径
			File picture = FileUtils.createNewTempFileByUrl("temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
			return;
		}
		if (data == null)
			return;
		// 读取相册缩放图片
		if (requestCode == PHOTO_ZOOM) {
			startPhotoZoom(data.getData());
			return;
		}
		// 处理结果
		if (requestCode == PHOTO_RESOULT) {
//			Bundle extras = data.getExtras();
//			if (extras != null) {
//				Bitmap photo = extras.getParcelable("data");
				BitmapFactory.Options options = new BitmapFactory.Options();  
//            	options.inSampleSize = 2;  
				Bitmap photo = BitmapFactory.decodeFile(path, options); 
				int compressScale = 90;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, compressScale, stream);// (0-100)压缩文件
				while (stream.toByteArray().length / 1024 > 100) { 
					stream.reset();
                    compressScale -= 10;
                    photo.compress(Bitmap.CompressFormat.JPEG, compressScale, stream);
				}
				byte[] bytes = stream.toByteArray();
				Utility.closeSilently(stream);
				callback(CODE_BITMAP, photo, bytes);
//			}
			return;
		}
	}

	/**
	 * 收缩图片
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", false);
		File file = FileUtils.createNewTempFileByUrl("temp1.jpg");
		path = file.getAbsolutePath();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra("noFaceDetection", true); 
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//返回格式  
		startActivityForResult(intent, PHOTO_RESOULT);
	}
	
	private void callback(int code) {
		callback(code, null, null);
	}
	
	private void callback(int code, Bitmap bitmap, byte[] bytes) {
		Intent i = new Intent();
		i.putExtra(CALLBACK_DATAS_BYTES, bytes);
		setResult(code, i);
		this.finish();
	}

}