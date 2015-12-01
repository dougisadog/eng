package com.shuangge.english.view.read;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.network.read.TaskReqReadCore46Guide;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.BaseShadowMask;
import com.shuangge.english.view.component.photograph.MyViewPager;
import com.shuangge.english.view.read.fragment.BaseWordListFragment;
import com.shuangge.english.view.read.fragment.FragmentHasLearnWords;
import com.shuangge.english.view.read.fragment.FragmentNotStudyWords;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 单次列表 
 * @author doug
 *
 */
public class AtyWordList extends AbstractAppActivity implements OnClickListener {

	private ImageButton btnBack;
	private LinearLayout llTitleContainer, mainContainer;
	private MyViewPager vp;
	private int index = 0;
	private List<BaseWordListFragment> fragments;
	private List<TextView> titles;
	private TextView currentTitle , txtNext;
	
	private BaseShadowMask mask2;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_word_list);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		// btnBack.setImageDrawable(R.drawable.s_btn_back1);
		btnBack.setOnClickListener(this);
		
		txtNext = (TextView) findViewById(R.id.txtNext);
		txtNext.setOnClickListener(this);
		
		TextView txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
		txtBarTitleCn.setText("单词列表");
		TextView txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
		txtBarTitleEn.setText("WordsList");

		llTitleContainer = (LinearLayout) findViewById(R.id.llTitleContainer);
		llTitleContainer.setVisibility(View.GONE);

		initTitles();

		vp = (MyViewPager) findViewById(R.id.vp);
		vp.setAdapter(new SecretMsgListPagerAdapter(getSupportFragmentManager()));
		vp.setOffscreenPageLimit(2);
		vp.setCurrentItem(index);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
		
				fragments.get(position).initDatas();
				refreshTitle(position);
				
				//从服务器上请求数据过来 , 如果请求到了数据 就能进行 显示的操作 
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}

		});
		
		mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
		LessonTips lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
		if (!lessonTips.getWordList()) {
			lessonTips.setWordList(true);
			mask2 = new BaseShadowMask(new BaseShadowMask.CallbackHomeMask() {
	
				@Override
				public void close() {
					if (null != mask2) { 
						mask2.hide(getSupportFragmentManager());
						mask2 = null;
					}
				}
	
			}, (ViewGroup) mainContainer.getParent(), getString(R.string.readFourGuide), "确定");
			mask2.show(getSupportFragmentManager());
			new TaskReqReadCore46Guide(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
				}
				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			}, lessonTips.getCore46());
		}
	}
	
	@Override
	protected void onResume() {
		if (GlobalRes.getInstance().getBeans().getReadNewWordsResult().getDatas().size() == 0) {
			finish();
		}
		super.onResume();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.txtNext:
		AtyReadLesson.startAty(this, 1);
		default: 
			break;
		}

	}

	private void refreshTitle(int position) {
		if (null != currentTitle) {
			currentTitle.setTextColor(0xff16727F);
			currentTitle.setBackgroundResource(R.drawable.bg_class_settings);
			currentTitle.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
		}
		TextView txt = titles.get(position);
		txt.setTextColor(0xff26c6da);
		txt.setBackgroundResource(R.drawable.bg_class_settings3);
		txt.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
		currentTitle = txt;
		// notice();
	}

	private void initTitles() {

		fragments = new ArrayList<BaseWordListFragment>();
		fragments.add(new FragmentHasLearnWords());
//		fragments.add(new FragmentNotStudyWords());
		fragments.get(index).initDatas();

		TextView txt = null;
		int i = 0;
		titles = new ArrayList<TextView>();
		for (BaseWordListFragment fragment : fragments) {
			txt = new TextView(this);
			txt.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			txt.setTextSize(14);
			TextPaint tp = txt.getPaint();
			tp.setFakeBoldText(true);
			if (i == index) {
				txt.setTextColor(0xff23B6CA);
				txt.setBackgroundResource(R.drawable.bg_class_settings3);
				currentTitle = txt;
			} else {
				txt.setBackgroundResource(R.drawable.bg_class_settings);
				txt.setTextColor(0xff16727F);
			}
			
			if (fragment instanceof FragmentHasLearnWords) {
				txt.setText("错题本单词");
			} 
//			else if (fragment instanceof FragmentNotStudyWords) {
//				txt.setText("未学单词");
//			}
			LinearLayout.LayoutParams p = ViewUtils.setLinearMargins(txt, 0, LinearLayout.LayoutParams.MATCH_PARENT, 0,
					0, 0, -1);
			p.weight = 1;
			txt.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
			txt.setTag(i++);
			txt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					vp.setCurrentItem(index, true);
				}

			});
			llTitleContainer.addView(txt);
			titles.add(txt);
		}
	}

	/************************************************************************************************/
	public class SecretMsgListPagerAdapter extends FragmentPagerAdapter {
		public SecretMsgListPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return super.getPageTitle(position);
			// return titleList.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			return super.instantiateItem(container, position);
		}
	}

	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyWordList.class));
	}

}
