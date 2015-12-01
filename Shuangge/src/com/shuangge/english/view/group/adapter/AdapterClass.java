package com.shuangge.english.view.group.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.group.AtyClassCreate;

public class AdapterClass extends ArrayAdapter<ClassData> {

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<ClassData> datas = new ArrayList<ClassData>();
	
	private boolean showCreateBtn = false;
	private Activity aty;

	public AdapterClass(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterClass(Activity context, boolean showCreateBtn) {
		super(context, 0);
		this.showCreateBtn = showCreateBtn;
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public AdapterClass(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ClassData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ClassViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_class, null, true);
			holder = new ClassViewHolder();
			holder.imgHead = (ImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtRanklist = (TextView) convertView.findViewById(R.id.txtRanklist);
			holder.txtMemberNum = (TextView) convertView.findViewById(R.id.txtMemberNum);
			holder.txtScore = (TextView) convertView.findViewById(R.id.txtScore);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			holder.rlCreate = (RelativeLayout) convertView.findViewById(R.id.rlCreate);
			holder.txtWeChat = (TextView) convertView.findViewById(R.id.txtWeChat);
			convertView.setTag(holder);
		} 
		else {
			holder = (ClassViewHolder) convertView.getTag();
		}
		ClassData entity = datas.get(position);
		if (position == 0 && showCreateBtn && null != aty) {
			holder.rlCreate.setVisibility(View.VISIBLE);
			holder.rlCreate.setOnClickListener(onClickListener);
		}
		else {
			holder.rlCreate.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtName.setText(entity.getName().toString());
		}
		if (null != entity.getSignature()) {
			holder.txtSignature.setText(entity.getSignature());
		} 
		else {
			holder.txtSignature.setText("无");
		}
		if (null != entity.getLastWeekNo() && entity.getLastWeekNo() > 0) {
			holder.txtRanklist.setText(entity.getLastWeekNo().toString());
		} 
		else {
			holder.txtRanklist.setText("999+");
		}
		if (null != entity.getScore()) {
			holder.txtScore.setText("" + entity.getScore().toString() + "");
		}
		holder.txtMemberNum.setText("" + entity.getNum() + " / " + entity.getMaxNum());
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), holder.imgHead));
		} 
		else {
			holder.imgHead.setImageResource(R.drawable.bg_class_home);
		}
		if(null != entity.getWechatNo()){
			holder.txtWeChat.setText(entity.getWechatNo().toString());
		}
		return convertView;
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rlCreate:
				InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
				boolean conditions1 = data.getHonorData().getLevel() >= 1;
				boolean conditions2 = !data.isVisitor();
				boolean conditions3 = GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() == 0;
				boolean canCreate = conditions2 && conditions3;
				if (!canCreate) {
					Toast.makeText(getContext(), R.string.bindingAccountErrTip, Toast.LENGTH_LONG).show();
					AtyBindingAccount.startAty(aty);
				}
				else {
					AtyClassCreate.startAty(aty);
				}
				break;
			}
		}
		
	};

	public List<ClassData> getDatas() {
		return datas;
	}

	public final class ClassViewHolder {

		private ImageView imgHead;
		private TextView txtName, txtScore, txtMemberNum, txtSignature, txtRanklist,txtWeChat;
		private RelativeLayout rlCreate;

	}

}
