package com.shuangge.english.view.post.adapter;

import java.util.ArrayList;
import java.util.List;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.OnViewFlowTouchedListener;
import org.taptwo.android.widget.ViewFlow.OnViewFlowoInterceptTouchListener;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.entity.server.user.RewardsData;
import com.shuangge.english.network.rewards.TaskReqRewardsShare;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.ShareContentWebPage;
import com.shuangge.english.support.app.ShareManager.ShareConstants;
import com.shuangge.english.support.app.ShareManager1;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.setting.SettingHelper;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.task.TaskReqRewardsTip;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.group.AtyClassInvite;
import com.shuangge.english.view.group.AtyClassMember;
import com.shuangge.english.view.group.AtyClassSettings;
import com.shuangge.english.view.group.component.MembersContainer;
import com.shuangge.english.view.post.AtyPostEdit;
import com.shuangge.english.view.ranklist.AtyRanklist;
import com.shuangge.english.view.rewards.AtyRewardsLastweekRank;
import com.shuangge.english.view.rewards.component.DialogRewardsFragment;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.weixin.media.CircleShareContent;

public class AdapterPostForClass extends ArrayAdapter<Object> {
	
	public static final String SHOW_REWARDS_TIP_IN_CLASS = "show rewards tip in class";
	
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_TOP_ITEM = 1;
	private static final int TYPE_MAX_COUNT = 2;  
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Object> datas = new ArrayList<Object>();

	private CallbackPost callback;
	private Activity aty;
	private ImageView classRewards;
	private Boolean isClick = false;
	
//	private final UMSocialService mController = UMServiceFactory.getUMSocialService(ShareConstants.DESCRIPTOR);
	
	public static interface CallbackPost {
		
		public void addNice(PostData entity);
		
		public void removeNice(PostData entity);
		
		public void setCanScoll(boolean canScoll);
		
		public void showBg(ArrayList<String> urls, int index);
		
	}
	
	public AdapterPostForClass(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterPostForClass(Activity context, CallbackPost callback) {
		super(context, 0);
		this.aty = context;
		this.callback = callback;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterPostForClass(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_TOP_ITEM : TYPE_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PostViewHolder holder;
		int type = getItemViewType(position);
		if (type == TYPE_TOP_ITEM) {
			
			GlobalRes.getInstance().getBeans().setCurrentClassNo(GlobalRes.getInstance().getBeans().getCurrentMyClassNo());
			
			ClassData entity = (ClassData) datas.get(position);
			
			convertView = mInflater.inflate(R.layout.item_class_summary, null, true);
			
			LinearLayout llBg = (LinearLayout) convertView.findViewById(R.id.llBg);
			int h = AppInfo.getScreenHeight() - DensityUtils.dip2px(getContext(), 80);
			ViewUtils.setFrameMargins(llBg, FrameLayout.LayoutParams.MATCH_PARENT, h, 0, 0, 0, 0);
			llBg.setVisibility(View.INVISIBLE);
			if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() == 0) {
				return convertView;
			}
			llBg.setVisibility(View.VISIBLE);
			
			ViewFlow viewFlow = (ViewFlow) convertView.findViewById(R.id.vf);
			CircleFlowIndicator indic = (CircleFlowIndicator) convertView.findViewById(R.id.vfiDic);
			
			MembersContainer membersContainer = (MembersContainer) convertView.findViewById(R.id.membersContainer);
			membersContainer.setUrls(GlobalRes.getInstance().getBeans().getMemberData().getMembers());
			
			TextView txtLastRanklist = (TextView) convertView.findViewById(R.id.txtLastRanklist);
			TextView txtClassName = (TextView) convertView.findViewById(R.id.txtClassName);
			TextView txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			TextView txtWeekRanklist = (TextView) convertView.findViewById(R.id.txtWeekRanklist);
			TextView txtWeekScore = (TextView) convertView.findViewById(R.id.txtWeekScore);
			TextView txtMemberNum = (TextView) convertView.findViewById(R.id.txtMemberNum);
			ImageView imgLastRank = (ImageView) convertView.findViewById(R.id.lastRank);
			imgLastRank.setOnClickListener(clickListener);
			classRewards = (ImageView) convertView.findViewById(R.id.classReward);
			classRewards.setOnClickListener(clickListener);
			//班级奖励tip
			CacheBeans beans = GlobalRes.getInstance().getBeans();
			if (beans.getLoginData().getRewardsData().getAuth() == RewardsData.HAVE_AUTH) {
				classRewards.setVisibility(View.VISIBLE);
				if (!SettingHelper.getSharedPreferences(aty, SHOW_REWARDS_TIP_IN_CLASS, false)) {
					requestRewardsTip();
					SettingHelper.setEditor(aty, SHOW_REWARDS_TIP_IN_CLASS, true);
				}
			} 
			else {
				classRewards.setVisibility(View.GONE);
			}
			
			RelativeLayout rlInvite = (RelativeLayout) convertView.findViewById(R.id.rlInvite);
			rlInvite.setOnClickListener(clickListener);
			RelativeLayout rlWritePost = (RelativeLayout) convertView.findViewById(R.id.rlWritePost);
			rlWritePost.setOnClickListener(clickListener);
			LinearLayout llMembers = (LinearLayout) convertView.findViewById(R.id.llMembers);
			llMembers.setOnClickListener(clickListener);
			if (entity.getPhotoUrls() != null) {
				
				if (entity.getPhotoUrls().size() > 0) {
					urls = (ArrayList<String>) entity.getPhotoUrls();
				}
				if (entity.getPhotoUrls().size() > 1) {
					indic.setVisibility(View.VISIBLE);
				}
				else {
					indic.setVisibility(View.GONE);
				}
			}
			
			viewFlow.setAdapter(new ImageAdapter(getContext()));
			viewFlow.setFlowIndicator(indic);
			if (null != callback) {
				viewFlow.setOnViewFlowoInterceptTouchListener(new OnViewFlowoInterceptTouchListener() {
					
					@Override
					public void onMouseUp(MotionEvent e) {
					}
					
					@Override
					public void onMouseMove(MotionEvent e) {
					}
					
					@Override
					public void onMouseDown(MotionEvent e) {
						callback.setCanScoll(false);
					}
				});
				viewFlow.setOnViewFlowTouchedListener(new OnViewFlowTouchedListener() {
					
					@Override
					public void onMouseUp(MotionEvent e) {
						callback.setCanScoll(true);
					}
					
					@Override
					public void onMouseMove(MotionEvent e) {
					}
					
					@Override
					public void onMouseDown(MotionEvent e) {
					}
					
				});
			}
			
//			classInfoTip1.setText(data.getClassNo() + "");
			
			txtClassName.setText(entity.getName() + "");
			txtMemberNum.setText("+" + entity.getNum());
			txtWeekRanklist.setText(entity.getNo() < 0 ? "无" : entity.getNo() + "");
			txtWeekScore.setText(entity.getWeekScore() + "");
			if (TextUtils.isEmpty(entity.getSignature())) {
				txtSignature.setVisibility(View.GONE);
			}
			txtSignature.setText(TextUtils.isEmpty(entity.getSignature()) ? "无" : entity.getSignature());
			String lastRanklistStr = "100+";
			if (null != entity.getLastWeekNo() && entity.getLastWeekNo() > 0 && entity.getLastWeekNo() <= 100) {
				lastRanklistStr = entity.getLastWeekNo().toString();
			}
			txtLastRanklist.setText(lastRanklistStr);
			
			convertView.findViewById(R.id.llWeekRanklist).setOnClickListener(clickRanklistListener);
			
			return convertView;
		}
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_post, null, true);
			holder = new PostViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.imgFirst = (MaskImage) convertView.findViewById(R.id.imgFirst);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.imgTop = (ImageView) convertView.findViewById(R.id.imgTop);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtReplyNum = (TextView) convertView.findViewById(R.id.txtReplyNum);
			holder.txtNiceNum = (TextView) convertView.findViewById(R.id.txtNiceNum);
			holder.flNiceNum = (FrameLayout) convertView.findViewById(R.id.flNiceNum);
			convertView.setTag(holder);
		} else {
			holder = (PostViewHolder) convertView.getTag();
		}
		PostData entity = (PostData) datas.get(position);
		if (null == holder) {
		}
		holder.imgHead.setTag(entity.getUserNo());
		holder.imgHead.setOnClickListener(clickImgHeadListener);
		holder.imgTop.setVisibility(entity.getTop() ? View.VISIBLE : View.INVISIBLE);
		if (!TextUtils.isEmpty(entity.getTitle())) {
			holder.txtTitle.setText(entity.getTitle().toString());
		}
		else {
			holder.txtTitle.setVisibility(View.GONE);
		}
		if (null != entity.getCreateDate()) {
			holder.txtCreateDate.setText(DateUtils.convert(entity.getCreateDate()));
		}
		if (!TextUtils.isEmpty(entity.getUserName())) {
			holder.txtName.setText(entity.getUserName().toString());
		}
		if (!TextUtils.isEmpty(entity.getContent())) {
			holder.txtContent.setText(entity.getContent().toString());
		}
		holder.txtReplyNum.setText(" | " + entity.getReplyNum() + convertView.getResources().getString(R.string.postDetailsTip4));
		if (!TextUtils.isEmpty(entity.getUserHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getUserHeadUrl(), holder.imgHead));
		}
		else {
			holder.imgHead.clear();
		}
		if (!TextUtils.isEmpty(entity.getFirstPic())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getFirstPic(), holder.imgFirst));
			holder.imgFirst.setVisibility(View.VISIBLE);
		}
		else {
			holder.imgFirst.clear();
			holder.imgFirst.setVisibility(View.GONE);
		}
		if (entity.getNiceNum() <= 999) {
			holder.txtNiceNum.setText(entity.getNiceNum() + "");
		}
		else {
			holder.txtNiceNum.setText("999+");
		}
		holder.flNiceNum.setTag(entity);
		if (entity.isNiceForMe()) {
			holder.flNiceNum.setBackgroundResource(R.drawable.bg_nice_p);
			holder.flNiceNum.setOnClickListener(clickRemoveNiceListener);
		}
		else {
			holder.flNiceNum.setBackgroundResource(R.drawable.bg_nice);
			holder.flNiceNum.setOnClickListener(clickAddNiceListener);
		}
		return convertView;
	}
	
	private OnClickListener clickRanklistListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AtyRanklist.startAty((Activity) getContext(), 3);
		}
		
	};
	
	private OnClickListener clickImgHeadListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			long userNo = (Long) v.getTag();
			if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
				AtyUserInfo.startAty(getContext());
				return;
			}
			AtyBrowseUserInfo.startAty(getContext(), userNo);
		}
		
	};
	
	private OnClickListener clickAddNiceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			PostData entity = (PostData) v.getTag();
			if (null != callback) {
				callback.addNice(entity);
			}
		}
		
	};
	
	private OnClickListener clickRemoveNiceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			PostData entity = (PostData) v.getTag();
			if (null != callback) {
				callback.removeNice(entity);
			}
		}
		
	};
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.rlInvite:
				AtyClassInvite.startAty(aty);
				break;
			case R.id.rlWritePost:
				AtyPostEdit.startAtyForClass(aty);
				break;
			case R.id.llMembers:
				if (null != GlobalRes.getInstance().getBeans().getCurrentMyClassNo()) {
					if (GlobalRes.getInstance().getBeans().getCurrentClassNo().longValue() == GlobalRes.getInstance().getBeans().getCurrentMyClassNo().longValue()
							&& GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() >= ClassData.SUB_MANAGER) {
						AtyClassSettings.startAty(aty, 1);
					}
					else {
						AtyClassSettings.startAty(aty);
					}
				}
				else {
					AtyClassMember.startAty(aty);
				}
				break;
			case R.id.lastRank:
				AtyRewardsLastweekRank.startAty(aty);
				break;
			case R.id.classReward:
				if (!isClick) {
					requestRewardsTip();
					isClick = true;
				}
				break;
			}
		}
		
	};
	
	private void requestRewardsTip() {
		((AbstractAppActivity) aty).showLoading();
		new TaskReqRewardsTip(0, new CallbackNoticeView<Void, String>() {

			@Override
			public void refreshView(int tag, String result) {
				isClick = false;
				((AbstractAppActivity) aty).hideLoading();
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(aty, R.string.dialogRewardsErrTip, Toast.LENGTH_SHORT).show();
					return;
				}
				initRewardsDialog(result);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private DialogRewardsFragment rewardsDialog;
	
	private void initRewardsDialog(String htmlStr) {
		rewardsDialog = new DialogRewardsFragment(new DialogRewardsFragment.CallBackDialogConfirm() {
			
			@Override
			public void onClose() {
				rewardsDialog.dismiss();
				rewardsDialog = null;
			}

			@Override
			public void onSubmit() {
				wxq();
				rewardsDialog.dismiss();
				rewardsDialog = null;
//				classShare();
			}
			
		}, htmlStr);
		
		if (rewardsDialog.isVisible()) {
			return;
		}
		rewardsDialog.showDialog(((FragmentActivity) aty).getSupportFragmentManager());
		
	}
	
	//班级分享成功后调用  个人加钱 
	private boolean classShare(){
		new TaskReqRewardsShare(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

			@Override
			public void refreshView(int tag, Integer result) {
				((AbstractAppActivity) aty).hideLoading();
				if (null == result || result == TaskReqRewardsShare.STAUTS_NETWROK_ERR) {
					Toast.makeText(aty, R.string.dialogRewardsErrTip, Toast.LENGTH_SHORT).show();
					return;
				}
				if (null != rewardsDialog) {
					rewardsDialog.dismissAllowingStateLoss();
					rewardsDialog = null;
				}
				if (result == TaskReqRewardsShare.STAUTS_SUCCESS) {
					GlobalRes.getInstance().getBeans().getLoginData().getRewardsData().setAuth(RewardsData.RECEIVED);
					classRewards.setVisibility(View.GONE);
					double getMoney = GlobalRes.getInstance().getBeans().getLoginData().getRewardsData().getPersonRewards();
					//如果返回0元没有
					if (getMoney != 0) {
						Toast.makeText(aty, "上周您获得的"+ getMoney +"元奖学金已打入个人账户，请在个人中心查看", Toast.LENGTH_LONG).show();
					}
					return;
				}
				Toast.makeText(aty, "您已经领取过，请勿重复领取!", Toast.LENGTH_LONG).show();	
			}
			
		});
		
		return false;
		}
	
	public void wxq() {
		MyGroupListResult classDatas = GlobalRes.getInstance().getBeans().getMyGroupDatas();
		if (null == classDatas || null == classDatas.getClassInfos() || classDatas.getClassInfos().size() == 0) {
			return;
		}
		Long userNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo(); 
//		UMImage urlImage = new UMImage(aty, ConfigConstants.SHARE_IMG3);
//		CircleShareContent circleContent = new CircleShareContent();
//		circleContent.setShareContent(ConfigConstants.SHARE_CONTENT3);
//		circleContent.setTitle(ConfigConstants.SHARE_TITLE3);
//        circleContent.setTargetUrl(ConfigConstants.SHARE_URL4 + "/" + userNo);
//        circleContent.setShareMedia(urlImage);
//        mController.setShareMedia(circleContent);
//        mController.getConfig().closeToast();
//        mController.postShare(this.getContext(), SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
//        	
//			@Override
//			public void onStart() {
////				Toast.makeText(getContext(), "开始分享",Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
////				Toast.makeText(getContext(), "分享结束",Toast.LENGTH_SHORT).show();
//				mController.getConfig().cleanListeners();
//				if (eCode == 200) {
//					((AbstractAppActivity) aty).showLoading();
//					classShare();
//					return;
//				}
//				((AbstractAppActivity) aty).hideLoading();
//				String eMsg = "";
//				if (eCode == -101){
//					eMsg = "没有授权";
//				}
////				Toast.makeText(getContext(), "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//			}
//        });
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE3, 
        		ConfigConstants.SHARE_CONTENT3, 
        		ConfigConstants.SHARE_URL3 + "/" + userNo,
        		ConfigConstants.SHARE_IMG3);
        
        ShareManager1.getInstance(this.getContext()).shareByWeixin(wxContent, 1);
        //待修改
//        classShare();
	}
	
	public List<Object> getDatas() {
		return datas;
	}

	public final class PostViewHolder {
		
		private MaskImage imgFirst;
		private CircleImageView imgHead;
		private ImageView imgTop;
		private TextView txtName;
		private TextView txtTitle, txtContent;
		private TextView txtReplyNum, txtNiceNum;
		private TextView txtCreateDate;
		private FrameLayout flNiceNum;

	}
	
	/**********************************************************************************************************************/
	
	private ArrayList<String> urls = new ArrayList<String>();
	
	public class ImageAdapter extends BaseAdapter {
		
		public ImageAdapter(Context context) {
		}
	
		@Override
		public int getCount() {
			return urls.size() == 0 ? 1 : urls.size();
		}
	
		@Override
		public Object getItem(int position) {
			return position;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img = null;
			if (null == convertView) {
				img = new ImageView(getContext());
				img.setScaleType(ScaleType.CENTER_CROP);
				img.setOnClickListener(onClickPicListener);
			}
			else {
				img = (ImageView) convertView;
			}
			img.setTag(position);
			if (urls.size() == 0) {
				img.setImageResource(R.drawable.bg_class_home);
			}
			else {
				GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(urls.get(position), img));
			}
			return img;
		}

	}
	
	private OnClickListener onClickPicListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null != callback) {
				callback.setCanScoll(true);
			}
			if (urls.size() == 0) {
				return;
			}
			if (null != callback) {
				callback.showBg(urls, (Integer) v.getTag());
			}
			
		}
		
	};

	public ImageView getClassRewards() {
		return classRewards;
	}

	public void setClassRewards(ImageView classRewards) {
		this.classRewards = classRewards;
	}

}
