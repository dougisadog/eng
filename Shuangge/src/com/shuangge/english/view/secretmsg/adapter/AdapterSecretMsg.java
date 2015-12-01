package com.shuangge.english.view.secretmsg.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;

public class AdapterSecretMsg extends ArrayAdapter<SecretMsgDetailData> {

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<SecretMsgDetailData> datas = new ArrayList<SecretMsgDetailData>();
	public AdapterSecretMsg(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterSecretMsg(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public SecretMsgDetailData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SystemNoticetViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_secret_msg, null, true);
			holder = new SystemNoticetViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtDetails = (TextView) convertView.findViewById(R.id.txtDetails);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.imgUserHead = (CircleImageView) convertView.findViewById(R.id.imgUserHead);
			holder.imgContent = (MaskImage) convertView.findViewById(R.id.imgContent);
			holder.imgRedPoint = (ImageView) convertView.findViewById(R.id.imgRedPoint);
			convertView.setTag(holder);
		} 
		else {
			holder = (SystemNoticetViewHolder) convertView.getTag();
		}
		
		SecretMsgDetailData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getFriendName())) {
			holder.txtName.setText(entity.getFriendName().toString());
		}
		if (!TextUtils.isEmpty(entity.getContent())) {
			holder.txtDetails.setText(entity.getContent().toString());
		}
		else {
			holder.txtDetails.setText("[图片]");
		}
		if (null != entity.getSendTime()) {
			holder.txtCreateDate.setText(DateUtils.convert(entity.getSendTime()));
		}
		if (!TextUtils.isEmpty(entity.getFriendHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getFriendHeadUrl(), holder.imgUserHead));
		}
		else {
			holder.imgUserHead.clear();
		}
		if (null != entity.getStatus()) {
			holder.imgRedPoint.setVisibility(entity.getStatus() == SecretMsgDetailData.STATUS_UNREAD ? View.VISIBLE : View.GONE);
		}
		
		return convertView;
	}
	
	public List<SecretMsgDetailData> getDatas() {
		return datas;
	}
	
	public final class SystemNoticetViewHolder {
		
		private TextView txtName;
		private TextView txtCreateDate;
		private TextView txtDetails;
		private CircleImageView imgUserHead;
		private ImageView imgRedPoint;
		private MaskImage imgContent;

	}

}
