package com.shuangge.english.view.ranklist;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.ranklist.ClassRanklistData;
import com.shuangge.english.entity.server.ranklist.RanklistData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.network.ranklist.TaskReqRanklistAttention;
import com.shuangge.english.network.ranklist.TaskReqRanklistAttentionLastWeek;
import com.shuangge.english.network.ranklist.TaskReqRanklistAttentionWeek;
import com.shuangge.english.network.ranklist.TaskReqRanklistClass;
import com.shuangge.english.network.ranklist.TaskReqRanklistClassLastWeek;
import com.shuangge.english.network.ranklist.TaskReqRanklistClassWeek;
import com.shuangge.english.network.ranklist.TaskReqRanklistUser;
import com.shuangge.english.network.ranklist.TaskReqRanklistUserLastWeek;
import com.shuangge.english.network.ranklist.TaskReqRanklistUserWeek;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.ComponentMultiSwitch;
import com.shuangge.english.view.component.ComponentMultiSwitch.onSelectedListener;
import com.shuangge.english.view.group.AtyBrowseClassInfo;
import com.shuangge.english.view.ranklist.adapter.AdapterClassRanklistData;
import com.shuangge.english.view.ranklist.adapter.AdapterPersonalRanklist;
import com.shuangge.english.view.ranklist.adapter.AdapterUserRanklistData;
import com.shuangge.english.view.ranklist.adapter.IAdapterData;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyRanklist extends AbstractAppActivity implements OnClickListener, OnItemClickListener, CallbackNoticeView<Void, Boolean> {

	public static final int REQUEST_RANKLIST = 1040;
	public static final String RANKLIST_TYPE = "ranklist_type"; 
	
//	private TitleBar titleBar;
	private ImageButton btnBack;

	private ComponentMultiSwitch ms;
	private ImageView imgPersonal, imgAttention, imgClass;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterPersonalRanklist adapter;
	
//	private String[] ranklistTitles;
	
	private TextView txtNo, txtName, txtScore;
	private ImageView imgNo;
	private CircleImageView imgHead;
	private RelativeLayout rlUserInfo;
	
	private int pageNo = 1;
	private int msType = 0;
	private int ranklistType = 1;
	private int classNo = 0;
	private ImageView currentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_ranklist);
		
		ranklistType = getIntent().getIntExtra(RANKLIST_TYPE, 1);
		
		imgPersonal = (ImageView) findViewById(R.id.imgPersonal);
		imgPersonal.setOnClickListener(this);
		imgAttention = (ImageView) findViewById(R.id.imgAttention);
		imgAttention.setOnClickListener(this);
		imgClass = (ImageView) findViewById(R.id.imgClass);
		imgClass.setOnClickListener(this);
		switch (ranklistType % 3) {
		case 1:
			currentType = imgPersonal;
			imgPersonal.setImageResource(R.drawable.icon_ranklist_personal_p);
			break;
		case 2:
			currentType = imgAttention;
			imgAttention.setImageResource(R.drawable.icon_ranklist_attention_p);
			break;
		default:
			currentType = imgClass;
			imgClass.setImageResource(R.drawable.icon_ranklist_class_p);
			break;
		}
		ClassData classData = getClassData();
		if (null != classData) {
			classNo = classData.getClassNo().intValue();
		}
		
		ms = (ComponentMultiSwitch) findViewById(R.id.msType);
		ms.setVal((ranklistType - 1) / 3);
		ms.setOnSelectedListener(new onSelectedListener() {
			
			@Override
			public void onSelected(View v, int index) {
				msType = index * 3;
				pageNo = 1;
				
				if (imgPersonal.equals(currentType)) {
					ranklistType = msType + 1;
				}
				else if (imgAttention.equals(currentType)) {
					ranklistType = msType + 2;
				}
				else {
					ranklistType = msType + 3;
				}
				requestDatas();
			}
			
		});
		
//		titleBar = (TitleBar) findViewById(R.id.titleBar);
//		ranklistTitles = getResources().getStringArray(R.array.ranklistTitles);
//		titleBar.setSpinnerTitleDatas(ranklistTitles);
//		titleBar.getSpinnerTitle().setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				ranklistType = position;
//				pageNo = 1;
//				requestDatas();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtNo = (TextView) findViewById(R.id.txtNo);
		txtName = (TextView) findViewById(R.id.txtName);
		txtScore = (TextView) findViewById(R.id.txtScore);
		imgNo = (ImageView) findViewById(R.id.imgNo);
		imgHead = (CircleImageView) findViewById(R.id.imgHead);
		rlUserInfo = (RelativeLayout) findViewById(R.id.rlUserInfo);
		rlUserInfo.setOnClickListener(this);

		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		// set mode to BOTH
		refreshListView.setMode(Mode.BOTH);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...
		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
					if (refreshListView.isHeaderShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						pageNo = 1;
						requestDatas();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						++pageNo;
						requestDatas();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterPersonalRanklist(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		
		requestDatas();
	}
	
	private Animation getAnimation() {
		Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f); 
		alphaAnimation.setDuration(500);
		return alphaAnimation;
	}
	
	private void requestDatas() {
		showLoading();
		switch (ranklistType) {
		case 1:
			new TaskReqRanklistUserWeek(0, AtyRanklist.this, pageNo);
			break;
		case 2:
			new TaskReqRanklistAttentionWeek(0, AtyRanklist.this, pageNo);
			break;
		case 3:
			new TaskReqRanklistClassWeek(0, AtyRanklist.this, pageNo, classNo);
			break;
		case 4:
			new TaskReqRanklistUser(0, AtyRanklist.this, pageNo);
			break;
		case 5:
			new TaskReqRanklistAttention(0, AtyRanklist.this, pageNo);
			break;
		case 6:
			new TaskReqRanklistClass(0, AtyRanklist.this, pageNo, classNo);
			break;
		case 7:
			new TaskReqRanklistUserLastWeek(0, AtyRanklist.this, pageNo);
			break;
		case 8:
			new TaskReqRanklistAttentionLastWeek(0, AtyRanklist.this, pageNo);
			break;
		case 9:
			new TaskReqRanklistClassLastWeek(0, AtyRanklist.this, pageNo, classNo);
			break;
		}
	}
	
	private void initMyself(InfoData entity, Integer no, Integer score) {
		rlUserInfo.setVisibility(View.VISIBLE);
		txtNo.setVisibility(View.INVISIBLE);
		imgNo.setVisibility(View.INVISIBLE);
		if (null != no && no.intValue() > 0) {
			if (no.intValue() < 4) {
				switch (no.intValue()) {
				case 1:
					imgNo.setImageResource(R.drawable.icon_ranklist_no1);
					break;
				case 2:
					imgNo.setImageResource(R.drawable.icon_ranklist_no2);
					break;
				case 3:
					imgNo.setImageResource(R.drawable.icon_ranklist_no3);
					break;
				}
				imgNo.setVisibility(View.VISIBLE);
			}
			else {
				String noStr = no.toString();
				if (no > 999) {
					noStr = "999+";
				}
				txtNo.setText(noStr);
				txtNo.setVisibility(View.VISIBLE);
			}
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			txtName.setText(entity.getName().toString());
		}
		if (null != score) {
			txtScore.setText(score.toString());
		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), imgHead));
		}
		else {
			imgHead.clear();
		}
	}
	
	private void initMyClassData(ClassData entity, Integer no, Integer score) {
		rlUserInfo.setVisibility(View.VISIBLE);
		txtNo.setVisibility(View.INVISIBLE);
		imgNo.setVisibility(View.INVISIBLE);
		if (null != no) {
			if (no.intValue() < 4) {
				switch (no.intValue()) {
				case 1:
					imgNo.setImageResource(R.drawable.icon_ranklist_no1);
					break;
				case 2:
					imgNo.setImageResource(R.drawable.icon_ranklist_no2);
					break;
				case 3:
					imgNo.setImageResource(R.drawable.icon_ranklist_no3);
					break;
				}
				imgNo.setVisibility(View.VISIBLE);
			}
			else {
				String noStr = no.toString();
				if (no > 999 || no <= 0) {
					noStr = "999+";
				}
				txtNo.setText(noStr);
				txtNo.setVisibility(View.VISIBLE);
			}
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			txtName.setText(entity.getName().toString());
		}
		if (null != score) {
			txtScore.setText(score.toString());
		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), imgHead));
		}
		else {
			imgHead.clear();
		}
	}
	
	private void refreshDatas(List<IAdapterData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
		refreshListView.getRefreshableView().setSelection(1);
	}
	
	private void addDatas(List<IAdapterData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.rlUserInfo:
			AtyUserInfo.startAty(this);
			break;
		case R.id.imgPersonal:
			clearPrevType();
			currentType = imgPersonal;
			imgPersonal.setImageResource(R.drawable.icon_ranklist_personal_p);
			ranklistType = msType + 1;
			requestDatas();
			break;
		case R.id.imgAttention:
			clearPrevType();
			currentType = imgAttention;
			imgAttention.setImageResource(R.drawable.icon_ranklist_attention_p);
			ranklistType = msType + 2;
			requestDatas();
			break;
		case R.id.imgClass:
			clearPrevType();
			currentType = imgClass;
			imgClass.setImageResource(R.drawable.icon_ranklist_class_p);
			ranklistType = msType + 3;
			requestDatas();
			break;
		}
	}
	
	private void clearPrevType() {
		pageNo = 1;
		if (imgPersonal.equals(currentType)) {
			currentType.setImageResource(R.drawable.icon_ranklist_personal);
		}
		else if (imgAttention.equals(currentType)) {
			currentType.setImageResource(R.drawable.icon_ranklist_attention);
		}
		else if (imgClass.equals(currentType)) {
			currentType.setImageResource(R.drawable.icon_ranklist_class);
		}
	}

	@Override
	public void refreshView(int tag, Boolean result) {
		hideLoading();
		if (null == result || !result) {
			refreshListView.onRefreshComplete2();
			return;
		}
		if (refreshListView.isReFreshingForDown()) {
			addDatas(getRanklistData());
			return;
		}
		refreshDatas(getRanklistData());
//		refreshListView.startAnimation(animation);
	}
	
	@Override
	public void onProgressUpdate(int tag, Void[] values) {
		
	}

	private List<IAdapterData> getRanklistData() {
		List<IAdapterData> datas = new ArrayList<IAdapterData>();
		switch (ranklistType) {
		case 1:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getUserWeekRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getUserWeekRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getUserWeekRanklistData().getMyScore());
			break;
		case 2:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getAttentionWeekRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getAttentionWeekRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getAttentionWeekRanklistData().getMyScore());
			break;
		case 3:
			for (ClassRanklistData data : GlobalRes.getInstance().getBeans().getClassWeekRanklistData().getRanklist()) {
				datas.add(new AdapterClassRanklistData(data));
			}
			ClassData classData = getClassData();
			if (null == classData) {
				rlUserInfo.setVisibility(View.GONE);
				break;
			}
			if (pageNo == 1)
				initMyClassData(classData, GlobalRes.getInstance().getBeans().getClassWeekRanklistData().getMyClassNo(), GlobalRes.getInstance().getBeans().getClassWeekRanklistData().getMyClassScore());
			break;
		case 4:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getUserRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getUserRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getUserRanklistData().getMyScore());
			break;
		case 5:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getAttentionRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getAttentionRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getAttentionRanklistData().getMyScore());
			break;
		case 6:
			for (ClassRanklistData data : GlobalRes.getInstance().getBeans().getClassRanklistData().getRanklist()) {
				datas.add(new AdapterClassRanklistData(data));
			}
			classData = getClassData();
			if (null == classData) {
				rlUserInfo.setVisibility(View.GONE);
				break;
			}
			if (pageNo == 1)
				initMyClassData(classData, GlobalRes.getInstance().getBeans().getClassRanklistData().getMyClassNo(), GlobalRes.getInstance().getBeans().getClassRanklistData().getMyClassScore());
			break;
		case 7:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getUserLastWeekRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getUserLastWeekRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getUserLastWeekRanklistData().getMyScore());
			break;
		case 8:
			for (RanklistData data : GlobalRes.getInstance().getBeans().getAttentionLastWeekRanklistData().getRanklist()) {
				datas.add(new AdapterUserRanklistData(data));
			}
			if (pageNo == 1)
				initMyself(GlobalRes.getInstance().getBeans().getLoginData().getInfoData(), GlobalRes.getInstance().getBeans().getAttentionLastWeekRanklistData().getMyNo(), GlobalRes.getInstance().getBeans().getAttentionLastWeekRanklistData().getMyScore());
			break;	
		case 9:
			for (ClassRanklistData data : GlobalRes.getInstance().getBeans().getClassLastWeekRanklistData().getRanklist()) {
				datas.add(new AdapterClassRanklistData(data));
			}
			classData = getClassData();
			if (null == classData) {
				rlUserInfo.setVisibility(View.GONE);
				break;
			}
			if (pageNo == 1)
				initMyClassData(classData, GlobalRes.getInstance().getBeans().getClassLastWeekRanklistData().getMyClassNo(), GlobalRes.getInstance().getBeans().getClassLastWeekRanklistData().getMyClassScore());
			break;
		}
		return datas;
	}
	
	private ClassData getClassData() {
		ClassData classData = null;
		if (null != GlobalRes.getInstance().getBeans().getMyGroupDatas() && GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() > 0) {
			classData = GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0);
		}
		return classData;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IAdapterData data = adapter.getItem(position - 1);
		switch (data.getType()) {
		case IAdapterData.TYPE_USER:
			if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == data.getIndex().longValue()) {
				AtyUserInfo.startAty(this);
			} else {
				AtyBrowseUserInfo.startAty(this, data.getIndex());
			}
			
			break;
		case IAdapterData.TYPE_CLASS:
			AtyBrowseClassInfo.startAty(this, data.getIndex());
			break;
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyRanklist.class);
		context.startActivityForResult(i, REQUEST_RANKLIST);
	}
	
	public static void startAty(Activity context, int type) {
		Intent i = new Intent(context, AtyRanklist.class);
		i.putExtra(RANKLIST_TYPE, type);
		context.startActivityForResult(i, REQUEST_RANKLIST);
	}
}
