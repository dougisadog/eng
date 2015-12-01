package com.shuangge.english.view.about.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.view.about.model.AppDataModel;
import com.shuangge.english.view.component.ComponentLevel;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
@SuppressLint("NewApi") 
public class AdapterApp extends ArrayAdapter<AppDataModel> {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<AppDataModel> datas = new ArrayList<AppDataModel>();
	private Activity aty;
	private Bitmap bmp;
	public AdapterApp(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterApp(Activity context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public AppDataModel getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		UserViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_app, null, true);
			holder = new UserViewHolder();
			holder.imgHead = (ImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			holder.txtDown = (TextView) convertView.findViewById(R.id.txtDown);
//			holder.txtDown.setOnClickListener(this);
//			holder.rl.setOnClickListener(this);
			convertView.setTag(holder);
		} 
		else {
			holder = (UserViewHolder) convertView.getTag();
		}
		AppDataModel entity = datas.get(position);
		if (null == holder) {
			DebugPrinter.e(datas.size() +  "-----" +position);
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtName.setText(entity.getName());
		}
		if (!TextUtils.isEmpty(entity.getDetail())) {
			holder.txtSignature.setText(entity.getDetail());
		}
		
//		if (!TextUtils.isEmpty(entity.getWeekScore())) {
//			holder.txtScore.setText(entity.getWeekScore());
//		}
//		else {
//			holder.txtScore.setText("");
//		}
		if (!TextUtils.isEmpty(entity.getImgUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getImgUrl(), holder.imgHead));
		}
		else {
//			holder.imgHead.clear();
		}
		return convertView;
	}
	
	public List<AppDataModel> getDatas() {
		return datas;
	}
	
	public Bitmap getBmp() {
		return bmp;
	}
	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.txtDown:
//			AtyBrowseUserInfo.startAty(getContext(), (Long) v.getTag());
//			downApp();
//			break;
//		}
//		
//	}
	
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
//	@SuppressLint("NewApi") 
//	private void downApp() {
//		final NotificationManager notificationManager;
//		notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//		 final Notification.Builder builder3 = new Notification.Builder(
//				 getContext());
//         builder3.setSmallIcon(R.drawable.ic_launcher)
//                 .setTicker("showProgressBar").setContentInfo("contentInfo")
//                 .setOngoing(true).setContentTitle("ContentTitle")
//                 .setContentText("ContentText");
//         // 模拟下载过程
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//
//                 int progress = 0;
//
//                 for (progress = 0; progress < 100; progress += 5) {
//                     // 将setProgress的第三个参数设为true即可显示为无明确进度的进度条样式
//                     builder3.setProgress(100, progress, false);
//
//                     notificationManager.notify(0, builder3.build());
//                     try {
//                         Thread.sleep(1 * 1000);
//                     } catch (InterruptedException e) {
//                         System.out.println("sleep failure");
//                     }
//                 }
//                 builder3.setContentTitle("Download complete")
//                         .setProgress(0, 0, false).setOngoing(false);
//                 notificationManager.notify(0, builder3.build());
//
//             }
//         }).start();
//         
//	}

	public final class UserViewHolder {
		
		private ImageView imgHead;
		private TextView txtName, txtSignature, txtDown;
		private ComponentLevel level;
	}

}
