package com.shuangge.english.view.home.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ImageView圆形遮罩处理 background设置遮罩颜色 tag设置圆的边框色 padding设置圆形线宽
 * 
 * @author planet
 *
 */
public class HomeMask extends Fragment {
	
	public static interface CallbackHomeMask {
		
		public void close();
		
	}
	
	private ImageView ivMaskBg, ivMaskOk;
	private static CallbackHomeMask callback;
	private static View[] vs;
	private static ViewGroup vg;
	
	public HomeMask() {
		super();
	}
	
	public HomeMask(CallbackHomeMask callback, ViewGroup vg, View... vs) {
		super();
		HomeMask.callback = callback;
		HomeMask.vg = vg;
		HomeMask.vs = vs;
	}
	
	public void show(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(vg.getId(), this);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
	}
	
	public void hide(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(this);
		ft.commit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.component_home_mask, container, false);
        ivMaskBg = (ImageView) v.findViewById(R.id.ivMaskBg);
        ivMaskOk = (ImageView) v.findViewById(R.id.ivMaskOk);
        ivMaskOk.setOnClickListener(clickListener);
        v.setOnClickListener(clickListener);
        drawBg();
		return v;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ivMaskOk) {
				callback.close();
			}
		}
		
	};
	
	private void drawBg() {
		int width = vg.getMeasuredWidth();
		int height = vg.getMeasuredHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Path path = new Path(); 
		paint.setColor(0x00000000);
		paint.setStyle(Style.FILL);

		float radius = width / (float) 2;
		path.reset();
		path.moveTo(0, radius);
		path.lineTo(0, 0);
		path.lineTo(width, 0);
		path.lineTo(width, height);
		path.lineTo(0, height);
		path.lineTo(0, radius);
		// 圆弧左边中间起点是180,旋转360度
		path.close();
		canvas.drawPath(path, paint);
		paint.setColor(0x77000000);
		paint.setStyle(Style.FILL);
		int w = 0;
		int[][] points = new int[][] {{0, 0}, {width, 0}};
		int i = 0;
		for (View v : vs) {
			int[] location = new int[2];
			v.getLocationInWindow(location);
			if (w == 0) {
				w = v.getMeasuredWidth() > v.getMeasuredHeight() ? v.getMeasuredWidth() : v.getMeasuredHeight();
			}
			if (i > 0) 
				points[i][0] = points[i][0] - w;
			path.arcTo(new RectF(points[i][0], points[i][1], points[i][0] + w, points[i][1] + w), 180, -359, true);
			++i;
		}
		path.close();
		canvas.drawPath(path, paint);
		ivMaskBg.setImageBitmap(bitmap);
	}
}
