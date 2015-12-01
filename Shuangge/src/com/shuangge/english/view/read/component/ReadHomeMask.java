package com.shuangge.english.view.read.component;

import com.shuangge.english.support.utils.DensityUtils;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ImageView圆形遮罩处理 background设置遮罩颜色 tag设置圆的边框色 padding设置圆形线宽
 * 
 * @author planet
 *
 */
public class ReadHomeMask extends Fragment {
	
	public static interface CallbackHomeMask {
		
		public void close();
		
	}
	
	private static CallbackHomeMask callback;
	private static ViewGroup vg;
	
	private LinearLayout shadowCover;
	
	private static int SHADOW_COLOR = 0xBB000000;
	
	private FrameLayout fl, fl2, top;
	private LinearLayout l ,lbtn ;
	private TextView content ,btn;
	
	public ReadHomeMask() {
		super();
	}
	
	public ReadHomeMask(CallbackHomeMask callback, ViewGroup vg) {
		super();
		ReadHomeMask.callback = callback;
		ReadHomeMask.vg = vg;
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
        View v = inflater.inflate(R.layout.component_shadow_cover, container, false);
       
        shadowCover = (LinearLayout) v.findViewById(R.id.shadowCover);
        buildView(1);
        shadowCover.setOnClickListener(clickListener);
		return v;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null!= v.getTag() && v.getTag().equals("btn")) {
				callback.close();
			}
		}
		
	};
	
	/**
	 * 根据step 创建 对应的mask遮罩层view
	 * @param step 1~4 readhome 的引导步骤 若>4 则释放内存 退出
	 */
	@SuppressLint("ResourceAsColor")
	public void buildView(int step) {
		shadowCover.removeAllViews();

		if (null == top) {
			top = new FrameLayout(getActivity());
			top.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dip2px(getActivity(), 60)));
			top.setBackgroundColor(SHADOW_COLOR);
		}

		if (null == fl) {
			fl = new FrameLayout(getActivity());
			fl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
			fl.setBackgroundColor(0x00000000);
			fl.setOnClickListener(clickListener);
		}

		 if (null == content) {
			 content = new TextView(getActivity());
			 content.setGravity(Gravity.CENTER_HORIZONTAL);
			 content.setTextSize(20);
			 content.setTextColor(0xFFFFFFFF);
			 content.setGravity(Gravity.CENTER);
			 content.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
		 }
		 if (null == btn) {
			 btn = new TextView(getActivity());
		     btn.setText("next");
		     btn.setTextSize(20);
		     btn.setTextColor(0xFFFFFFFF);
		     btn.setTag("btn");
		     btn.setBackgroundResource(R.drawable.read_big_green);
		     btn.setGravity(Gravity.CENTER);
		     btn.setOnClickListener(clickListener);
		     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		     lp.setMargins(10, 10, 10, 10);
		     btn.setLayoutParams(lp); 
		 }
	     if (null == lbtn) {
	    	 lbtn = new LinearLayout(getActivity());
	    	 lbtn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
	    	 lbtn.setOrientation(LinearLayout.VERTICAL);
	    	 lbtn.addView(btn);
	     }
	     
		 if (null == l) {
			 l = new LinearLayout(getActivity());
			 l.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 2.0f));
			 l.setBackgroundColor(SHADOW_COLOR);
			 l.setOrientation(LinearLayout.VERTICAL);
			 l.addView(content);
		     l.addView(lbtn);
		 }
		
	     if (null == fl2) {
	    	 fl2 = new FrameLayout(getActivity());
	    	 fl2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
	    	 fl2.setBackgroundColor(SHADOW_COLOR);
	     }
	    
	     shadowCover.addView(top);
	     switch (step) {
		case 1:
			content.setText(R.string.readHomeGuideStep1);
			shadowCover.addView(fl);
		    shadowCover.addView(l);
		    shadowCover.addView(fl2);
			break;
		case 2:
			content.setText(R.string.readHomeGuideStep2);
			shadowCover.addView(fl2);
			shadowCover.addView(fl);
		    shadowCover.addView(l);
			break;
		case 3:
			content.setText(R.string.readHomeGuideStep3);
			shadowCover.addView(l);
			shadowCover.addView(fl);
		    shadowCover.addView(fl2);
			break;
		case 4:
			content.setText(R.string.readHomeGuideStep4);
			shadowCover.addView(fl2);
		    shadowCover.addView(l);
		    shadowCover.addView(fl);
			break;
		default:
			top = null;
			content = null;
			btn = null;
			lbtn = null;
			l = null;
			fl2 = null;
			fl = null;
			break;
		}
	}
}
