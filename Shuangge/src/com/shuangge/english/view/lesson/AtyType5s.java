package com.shuangge.english.view.lesson;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.EntityResType5;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.lesson.adapter.AdapterFragmentPager;
import com.shuangge.english.view.lesson.component.PositionContainer;
import com.shuangge.english.view.lesson.component.PositionContainer.PositionChanger;
import com.shuangge.english.view.lesson.fragment.FragmentType5;
import com.shuangge.english.view.lesson.fragment.FragmentType5_1;
import com.shuangge.english.view.lesson.fragment.FragmentType5_2;
import com.shuangge.english.view.lesson.fragment.FragmentType5_3;
import com.shuangge.english.view.lesson.fragment.FragmentType5_4;
import com.shuangge.english.view.lesson.fragment.FragmentType5_5;
import com.shuangge.english.view.lesson.fragment.FragmentType5_6;

/**
 * 模块列表
 * @author Jeffrey
 *
 */
public class AtyType5s extends AbstractAppActivity implements OnClickListener {

	private ImageButton btnBack;
	private PositionContainer positionContainer;

	private ViewPager viewPager;
	private MyViewPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_lesson_type5s);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		viewPager = (ViewPager) findViewById(R.id.vp);
		adapter = new MyViewPagerAdapter(getSupportFragmentManager());
		positionContainer = (PositionContainer) findViewById(R.id.positionContainer);
		positionContainer.setPositionChanger(new PositionChanger() {
			@Override
			public void refreshPositionView(int position) {
				viewPager.setCurrentItem(position);
			}
		});
		
		AtyBoss.starting = false;
		AtyLesson.starting = false;
		
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getCurrentType4Data() == null) {
			return;
		}
		List<EntityResType5> type5s = beans.getCurrentType4Data().getType5s();
		if (type5s.size() == 0) {
			return;
		}
		
		EntityResType5 goOnType5 = null;
		int goOnPos = 0;
		
		if (!TextUtils.isEmpty(type5s.get(0).getPageId())) {
			List<EntityResType5> datas = new ArrayList<EntityResType5>();
			String prePageId = null;
			int pageNo = 0;
			EntityResType5 data = null;
			for (int i = 0; i < type5s.size(); i++) {
				data = type5s.get(i);
				if (!data.getPageId().equals(prePageId)) {
					if (datas.size() > 0) {
						datas.add(data);
						adapter.getDatas().add(new FragmentType5(pageNo++, i == type5s.size(), prePageId, datas));
					}
					datas = new ArrayList<EntityResType5>();
					if (i > 0) {
						datas.add(type5s.get(i - 1));
					}
				}
				datas.add(data);
				prePageId = data.getPageId();
				
				//上次课程
				if (data.getId().equals(GlobalRes.getInstance().getBeans().getLastType5Id())) {
					goOnPos = i;
					goOnType5 = data;
				}
			}
			if (datas.size() > 0)
				adapter.getDatas().add(new FragmentType5(pageNo, true, prePageId, datas));
			positionContainer.setSize(pageNo + 1);
		}			
		//兼容旧版本
		else {
			if (!beans.getCurrentType4Data().isCore()) {
				if (type5s.size() >= 11) {
					positionContainer.setSize(5);
					adapter.getDatas().add(new FragmentType5_1());
					adapter.getDatas().add(new FragmentType5_2());
					adapter.getDatas().add(new FragmentType5_3());
					adapter.getDatas().add(new FragmentType5_4());
					adapter.getDatas().add(new FragmentType5_5());
				}
			}
			else {
				if (type5s.size() >= 2) {
					adapter.getDatas().add(new FragmentType5_6());
				}
			}
			
			int i = 0;
			for (EntityResType5 type5 : type5s) {
				++i;
				if (!type5.getId().equals(GlobalRes.getInstance().getBeans().getLastType5Id())) 
					continue;
				if (i <= 2) {
					goOnPos = 0;
				}
				else if (i <= 5) {
					goOnPos = 1;
				}
				else if (i <= 8) {
					goOnPos = 2;
				}
				else if (i <= 10) {
					goOnPos = 3;
				}
				else if (i <= 11) {
					goOnPos = 4;
				}
				goOnType5 = type5;
				break;
			}
			
		}
		
		if (adapter.getDatas().size() == 1)
			positionContainer.setVisibility(View.GONE);
		
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				positionContainer.setCurrentPos(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		boolean goOn = getIntent().getBooleanExtra("goOn", false);
		if (goOn && null != goOnType5) {
			viewPager.setCurrentItem(goOnPos);
			startLesson(goOnType5.getId(), goOnType5.getName());
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		}
	}
	
	public void startLesson(String id, String name) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		
		Type5Data type5 = beans.getUnlockDatas().getType5s().get(id);
		if (null == type5) {
			Toast.makeText(this, R.string.lessonTip3, Toast.LENGTH_LONG).show();
			return;
		}
		
		beans.setCurrentType5Id(id);
		
		beans.setLastType1Id(beans.getCurrentType1Id());
		beans.setLastType2Id(beans.getCurrentType2Id());
		beans.setLastType3Id(beans.getCurrentType3Id());
		beans.setLastType4Id(beans.getCurrentType4Id());
		beans.setLastType5Id(beans.getCurrentType5Id());
		
		EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(beans.getCurrentType2Id());
		if (null != type2)
			beans.setLastType2Name(type2.getName());
		EntityResType4 type4 = GlobalResTypes.ALL_TYPE4S_MAP.get(beans.getCurrentType4Id());
		if (null != type4)
			beans.setLastType4Name(type4.getName());
		beans.setLastType5Name(name);
		
		startLesson();
	}
	
	public void startLesson() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getCurrentType4Data().isCore()) {
			AtyBoss.startAty(this);
		}
		else {
			AtyLesson.startAty(this);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (resultCode != 0 && !TextUtils.isEmpty(GlobalRes.getInstance().getBeans().getCurrentType5Id())) {
			startLesson(beans.getCurrentType5Id(), beans.getLastType5Name());
		}
//		BaseType5 baseType5 = (BaseType5) adapter.getItem(viewPager.getCurrentItem());
//		baseType5.refreshView();
	}
	
	public class MyViewPagerAdapter extends AdapterFragmentPager {
		
		private List<Fragment> datas = new ArrayList<Fragment>();

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return datas.get(arg0);
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		public List<Fragment> getDatas() {
			return datas;
		}

	}
}
