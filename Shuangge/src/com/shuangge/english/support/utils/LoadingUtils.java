package com.shuangge.english.support.utils;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuangge.english.GlobalApp;

/**
 * 
 * @author Jeffrey
 *
 */
public class LoadingUtils {
	
	private static Dialog createLoadingDialog(Context context, String msg) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dlLoading);// 加载布局
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(GlobalApp.getInstance().getActivity(), R.anim.loading_animation);
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		Dialog loadingDialog = new Dialog(GlobalApp.getInstance().getActivity(), R.style.DialogLoadding);// 创建自定义样式dialog
		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return loadingDialog;

	}
	
	public static Dialog show(Dialog loadingDialog, Context context) {
		loadingDialog = createLoadingDialog(context, null);
		System.out.println(loadingDialog.getOwnerActivity());
		if (!loadingDialog.isShowing()) {
			loadingDialog.show();
		}
		return loadingDialog;
	}
	
	public static void hide(Dialog loadingDialog) {
		if (null == loadingDialog) {
			return;
		}
		loadingDialog.dismiss();
	}

}
