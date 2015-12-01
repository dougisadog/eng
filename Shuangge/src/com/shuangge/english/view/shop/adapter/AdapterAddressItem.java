package com.shuangge.english.view.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.network.shop.TaskReqAddressDefault;
import com.shuangge.english.network.shop.TaskReqAddressList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.shop.AtyShippingAddressEdit;
import com.shuangge.english.view.shop.AtyShopAddressList;
import com.shuangge.english.view.shop.AtyShopList;
import com.shuangge.english.view.shop.AtyShopOrderPay;

public class AdapterAddressItem extends ArrayAdapter<AddressData> implements OnClickListener{

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<AddressData> datas = new ArrayList<AddressData>();
	private Activity aty;
	private int pos;
	public AdapterAddressItem(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterAddressItem(Activity context, int resource) {
		super(context, resource);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public AddressData getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_address, null, true);
			holder = new SystemNoticetViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);
			holder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
			holder.flEdit = (FrameLayout) convertView.findViewById(R.id.flEdit);
//			holder.flDel = (FrameLayout) convertView.findViewById(R.id.flDel);
			holder.llSetDefault = (LinearLayout) convertView.findViewById(R.id.llSetDefault);
			holder.llSetDefault.setOnClickListener(this);
			holder.flEdit.setOnClickListener(this);
			convertView.setTag(holder);
		} 
		else {
			holder = (SystemNoticetViewHolder) convertView.getTag();
		}
		
		AddressData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getRecipient())) {
			holder.txtName.setText(entity.getRecipient().toString());
		}
		if (!TextUtils.isEmpty(entity.getDetailed())) {
			holder.txtaddress.setText(entity.getDetailed().toString());
		}
		if (!TextUtils.isEmpty(entity.getPhone())) {
			holder.txtPhone.setText(entity.getPhone().toString());
		}
		holder.llSetDefault.setTag(entity.getAddressId());
		holder.flEdit.setTag(entity);
		return convertView;
	}
	
	public List<AddressData> getDatas() {
		return datas;
	}
	
	
	public final class SystemNoticetViewHolder {
		
		private TextView txtaddress, txtPhone, txtName;
		private FrameLayout flEdit, flDel;
		private LinearLayout llSetDefault;

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llSetDefault:
			requestData((int) v.getTag());
			break;
		case R.id.flEdit:
			GlobalRes.getInstance().getBeans().setCurrentAddress((AddressData) v.getTag());
			AtyShippingAddressEdit.startAty(getContext());
			break;
		}
		
	}
	
	private void requestData(int id) {
		new TaskReqAddressDefault(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null == result || !result) {
					return;
				}
				Toast.makeText(aty, R.string.shopOrderTip7, Toast.LENGTH_SHORT).show();
				aty.finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, id);
	}

}
