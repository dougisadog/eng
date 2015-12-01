package com.shuangge.english.view.secretmsg;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.photograph.MyViewPager;
import com.shuangge.english.view.secretmsg.fragment.BaseSecretFriendFragment;
import com.shuangge.english.view.secretmsg.fragment.FragmentClassMembers;
import com.shuangge.english.view.secretmsg.fragment.FragmentSecretAttentions;
import com.shuangge.english.view.secretmsg.fragment.FragmentSecretFans;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtySecretFriendList extends AbstractAppActivity implements OnClickListener, OnItemClickListener {

	public static final int REQUEST_SECRET_FRIEND = 1075;
	private ImageButton btnBack;
	private LinearLayout llTitleContainer;
	private List<TextView> titles;
	private List<BaseSecretFriendFragment> fragments;
	private int index = 0;
	private TextView currentTitle;
	private MyViewPager vp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_secret_friend_list);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		llTitleContainer = (LinearLayout) findViewById(R.id.llTitleContainer);
		
		initTitles();
		
		vp = (MyViewPager) findViewById(R.id.vp);
		vp.setAdapter(new SecretMsgListPagerAdapter(getSupportFragmentManager()));
		vp.setOffscreenPageLimit(3);
		vp.setCurrentItem(index);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				fragments.get(position).initDatas();
				refreshTitle(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
			
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
	
	private void initTitles() {
		
		fragments = new ArrayList<BaseSecretFriendFragment>();
		fragments.add(new FragmentSecretAttentions());
		fragments.add(new FragmentSecretFans());
		if (GlobalRes.getInstance().getBeans().getCurrentMyClassNo() != null) {
			fragments.add(new FragmentClassMembers());
		}
		fragments.get(index).initDatas();
		
		TextView txt = null;
		int i = 0;
		titles = new ArrayList<TextView>();
		for (BaseSecretFriendFragment fragment : fragments) {
			txt = new TextView(this);
			txt.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			txt.setTextSize(14);
			TextPaint tp = txt.getPaint();
			tp.setFakeBoldText(true);
			if (i == index) {
				txt.setTextColor(0xff23B6CA);
				txt.setBackgroundResource(R.drawable.bg_class_settings3);
				currentTitle = txt;
			}
			else {
				txt.setBackgroundResource(R.drawable.bg_class_settings);
				txt.setTextColor(0xff16727F);
			}
			if (fragment instanceof FragmentSecretAttentions) {
				txt.setText("Attention");
			}
			else if (fragment instanceof FragmentSecretFans) {
				txt.setText("Fans");
			}
			else if (fragment instanceof FragmentClassMembers) {
				txt.setText("Class");
			}
			LinearLayout.LayoutParams p = ViewUtils.setLinearMargins(txt, 0, LinearLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, -1);
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
	
	private void refreshTitle(int position) {
		if (null != currentTitle) {
			currentTitle.setTextColor(0xff16727F);
			currentTitle.setBackgroundResource(R.drawable.bg_class_settings);
			currentTitle.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
		}
		TextView txt = titles.get(position);
		txt.setTextColor(0xff23B6CA);
		txt.setBackgroundResource(R.drawable.bg_class_settings3);
		txt.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
		currentTitle = txt;
//		notice();
	}
	
	/************************************************************************************************/
	public class SecretMsgListPagerAdapter extends FragmentPagerAdapter{
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
//			return titleList.get(position);
		}
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtySecretFriendList.class);
		context.startActivityForResult(i, REQUEST_SECRET_FRIEND);
	}
	
}