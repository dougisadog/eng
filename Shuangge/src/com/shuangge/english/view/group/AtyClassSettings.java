package com.shuangge.english.view.group;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.network.group.TaskReqLeave;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmWithPwdFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmWithPwdFragment.CallBackDialogConfirmWithPwd;
import com.shuangge.english.view.component.drag.DragGridView.OnDragListener;
import com.shuangge.english.view.component.photograph.MyViewPager;
import com.shuangge.english.view.group.fragment.BaseClassFragment;
import com.shuangge.english.view.group.fragment.FragmentClassInfoEdit;
import com.shuangge.english.view.group.fragment.FragmentClassMember;
import com.shuangge.english.view.group.fragment.FragmentClassMemberEdit;
import com.shuangge.english.view.group.fragment.FragmentClassMsg;
import com.shuangge.english.view.group.fragment.FragmentPost;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassSettings extends AbstractAppActivity implements OnClickListener, OnDragListener, IPushMsgNotice {

	public static final int REQUEST_CLASS_SETTINGS = 1006;
	public static final String PARAM_INDEX = "param index";
	
	private MyViewPager vp;
	private LinearLayout llTitleContainer;
	private List<TextView> titles;
	private TextView currentTitle;
	private List<BaseClassFragment> fragments;
	private ImageView btnQuit;
	private DialogConfirmWithPwdFragment dialogConfirm;
	private String password;
	private Long giveTo;
	private TextView txtSysMsgs;
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		fragments.clear();
		fragments = null;
		titles.clear();
		titles = null;
		currentTitle = null;
		vp = null;
	}
	
	@Override
	public boolean onBack() {
		return fragments.get(vp.getCurrentItem()).onBack();
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_class_settings);
		((ImageButton) findViewById(R.id.btnBack)).setOnClickListener(this);
		llTitleContainer = (LinearLayout) findViewById(R.id.llTitleContainer);
		
		btnQuit = (ImageView) findViewById(R.id.btnQuit);
		btnQuit.setOnClickListener(this);
		index = getIntent().getIntExtra(PARAM_INDEX, 0);
		fragments = new ArrayList<BaseClassFragment>();
		try {
			switch (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth()) {
			case ClassData.MANAGER:
				fragments.add(new FragmentClassInfoEdit());
				fragments.add(new FragmentClassMemberEdit());
				break;
			case ClassData.SUB_MANAGER:
				fragments.add(new FragmentClassMemberEdit());
				break;
			case ClassData.NORMAL:
				fragments.add(new FragmentClassMember());
				break;
			}
			fragments.add(new FragmentClassMsg());
			fragments.add(new FragmentPost());
			fragments.get(index).initDatas();
			initTitles();
		}
		catch (Exception e) {
			setResult(CODE_QUIT);
			AtyClassSettings.this.finish();
		}
		
		vp = (MyViewPager) findViewById(R.id.vp);
		vp.setAdapter(new ClassDetailsPagerAdapter(getSupportFragmentManager()));
		vp.setOffscreenPageLimit(4);
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
		
		
//		txtQuit = (TextView) findViewById(R.id.txtQuit);
//		txtQuit.setOnClickListener(this);
	}
	
	public void notice() {
		boolean flag = GlobalRes.getInstance().getBeans().getMsgDatas().isClassMsg();
		if (flag) {
			if (currentTitle.equals(txtSysMsgs)) {
				txtSysMsgs.setBackgroundResource(R.drawable.bg_class_settings3);
			}
			else {
				txtSysMsgs.setBackgroundResource(R.drawable.bg_class_settings2);
			}
		}
		else {
			if (currentTitle.equals(txtSysMsgs)) {
				txtSysMsgs.setBackgroundResource(R.drawable.bg_class_settings3);
			}
			else {
				txtSysMsgs.setBackgroundResource(R.drawable.bg_class_settings);
			}
		}
		txtSysMsgs.setPadding(0, 0, 0, DensityUtils.dip2px(this, 10));
	}
	
	private void initTitles() {
		TextView txt = null;
		int i = 0;
		titles = new ArrayList<TextView>();
		for (BaseClassFragment fragment : fragments) {
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
			if (fragment instanceof FragmentClassInfoEdit) {
				txt.setText("Details");
			}
			else if (fragment instanceof FragmentClassMemberEdit) {
				txt.setText("Members");
			}
			else if (fragment instanceof FragmentClassMember) {
				txt.setText("Members");
			}
			else if (fragment instanceof FragmentClassMsg) {
				txt.setText("Message");
			}
			else if (fragment instanceof FragmentPost) {
				txt.setText("My Post");
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
			if (fragment instanceof FragmentClassMsg) {
				txtSysMsgs = txt;
				notice();
			}
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
		notice();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				if (!fragments.get(vp.getCurrentItem()).onFinish()) {
					this.finish();
				}
				break;
			case R.id.btnQuit:
				dialogConfirm = new DialogConfirmWithPwdFragment(callBack , getString(R.string.classMemberDialogTip2En),
						getString(R.string.classMemberDialogTip2Cn), 0);
				dialogConfirm.showDialog(getSupportFragmentManager());
				break;
		}
	}
	
	private CallBackDialogConfirmWithPwd callBack = new CallBackDialogConfirmWithPwd() {

		@Override
		public void cancel() {
			hideDialogConfirm();
		}

		@Override
		public void submit(int position, String password) {
			hideDialogConfirm();
			AtyClassSettings.this.password = password;
			if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() == ClassData.MANAGER
					&& GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0).getNum() > 1) {
				AtyClassMemberForLeave.startAty(AtyClassSettings.this);
				return;
			}
			leave();
		}
		
	};
	
	private void leave() {
		new TaskReqLeave(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null == result || !result) {
					return;
				}
				Toast.makeText(AtyClassSettings.this, R.string.classLeaveSuccess, Toast.LENGTH_SHORT).show();
				setResult(CODE_QUIT);
				AtyClassSettings.this.finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, password, giveTo);
	}
	
	private void hideDialogConfirm() {
		if (null == dialogConfirm) {
			return;
		}
		dialogConfirm.dismiss();
		dialogConfirm = null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AtyClassMemberForLeave.REQUEST_CLASS_MEMBER_FOR_LEAVE 
				&& resultCode == AtyClassMemberForLeave.CODE_LEAVE) {
			giveTo = GlobalRes.getInstance().getBeans().getCurrentUserNo().longValue();
			leave();
			return;
		}
		fragments.get(vp.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
	}
	
	/************************************************************************************************************************/
	/**
	/**
	/**
	/************************************************************************************************************************/
	
	public class ClassDetailsPagerAdapter extends FragmentPagerAdapter {
		public ClassDetailsPagerAdapter(FragmentManager fm) {
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

	@Override
	public void onDrag() {
		vp.setCanScroll(false);
	}

	@Override
	public void onStopDrag() {
		vp.setCanScroll(true);
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassSettings.class);
		context.startActivityForResult(i, REQUEST_CLASS_SETTINGS);
	}
	
	public static void startAty(Activity context, int index) {
		Intent i = new Intent(context, AtyClassSettings.class);
		i.putExtra(PARAM_INDEX, index);
		context.startActivityForResult(i, REQUEST_CLASS_SETTINGS);
	}
	
}
