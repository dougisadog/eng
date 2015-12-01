package com.shuangge.english.view.about;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.MainActivity;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.applist.AppData;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.group.TaskReqRecommendMembers;
import com.shuangge.english.network.secretmsg.TaskReqAttentions;
import com.shuangge.english.network.shop.TaskReqUserSearch;
import com.shuangge.english.network.user.TaskReqSearchUsers;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.task.TaskAppDown;
import com.shuangge.english.task.TaskAppList;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.about.adapter.AdapterApp;
import com.shuangge.english.view.about.model.AppDataModel;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.secretmsg.adapter.AdapterFriend;
import com.shuangge.english.view.shop.AtyShopOrderPay;
import com.shuangge.english.view.shop.adapter.AdapterGiveUser;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.adapter.AdapterUser;

/**
 * 
 * @author Jeffrey
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") public class AtyAppList extends BaseAtyAppList implements
		OnClickListener, OnItemClickListener {

	public static final int REQUEST_APP_LIST = 1111;
	public static final String savePath = "/sdcard/Download/";  
	
	private boolean requesting = false;
	private ImageButton btnBack;
	private EditTextWidthTips txtSearch;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterApp adapter;
	private boolean searching = false;
	
	private Long userNo;
	
//	private Button but_down = null;
//    private ImageView iv_show = null;
//    private TextView tv_progress = null;
//    private ProgressBar pb = null;
    private NotificationManager manager = null;
//    private NotificationCompat.Builder builder;
    private NotificationCompat.Builder builder;
    private String headUrl;
    private String appUrl;
    private String appName;
    private Bitmap bmp;
    private File updateDir = null;
    private File updateFile = null;
    private boolean isDown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_app_list);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		manager = (NotificationManager) AtyAppList.this.getSystemService(NOTIFICATION_SERVICE);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
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
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						requestData();
						// Do work to refresh the list here.
						refreshListView.setStatusUp();
						
					} else if (refreshListView.isFooterShown()) {
							String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
							refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
							refreshListView.setStatusDown();
							requestData();
						}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
//		initAppListDatas();
		adapter = new AdapterApp(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		requestData();
	}
	
	private void requestData() {
		AssetManager asset = getAssets();
    	if(GlobalRes.getInstance().getBeans().getAppList() != null) {
    		refreshDatas(GlobalRes.getInstance().getBeans().getAppList());
    	}
    	new TaskAppList(0, new CallbackNoticeView<Void, List<AppDataModel>>() {

			@Override
			public void refreshView(int tag, List<AppDataModel> result) {
				if (null != result) {
//					appList = result;
					GlobalRes.getInstance().getBeans().setAppList(result);
					refreshDatas(result);
					return;
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, asset);
//		refreshDatas(appList);
	}
	

	private void refreshDatas(List<AppDataModel> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<AppDataModel> datas) {
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		headUrl = ((AppDataModel) adapter.getItem(position-1)).getImgUrl();
		appUrl = ((AppDataModel) adapter.getItem(position-1)).getAppUrl();
		appName = ((AppDataModel) adapter.getItem(position-1)).getName();
		if(GlobalRes.getInstance().getBeans().isDown())
		{
			Toast.makeText(AtyAppList.this,"正在下载，请稍候",0).show();
			return;
		}
		GlobalRes.getInstance().loadBitmap(headUrl, new GlobalRes.DownloadBitampListener() {
			
			@Override
			public void progressHandler(long progress, long max) {
				
			}
			
			@Override
			public void errorHandler(Exception e) {
				GlobalRes.getInstance().getBeans().setDown(false);
			}
			
			@Override
			public void completeHandler(Bitmap bitmap) {
				bmp = bitmap;
				if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
					updateDir = new File(Environment.getExternalStorageDirectory(), savePath + appName + ".apk");
					updateFile = new File(updateDir.getPath(),appName + ".apk");
//					isDown = true;
					GlobalRes.getInstance().getBeans().setDown(true);
//					new TaskAppDown(0, new CallbackNoticeView<Void, Boolean>() {
//
//					@Override
//					public void refreshView(int tag, Boolean result) {
//						if (null != result && result) {
//							return;
//						}
//					}
//		
//					@Override
//					public void onProgressUpdate(int tag, Void[] values) {
//					}
//					
//				}, appUrl);
					Toast.makeText(AtyAppList.this,appName + "开始下载",0).show();
					new DownloadImageTask().execute(appUrl);
				}
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == AtyClassInviteDetails.REQUEST_CLASS_INVITE_DETAILS) {
//			//TODO:Jeffrey 删除被邀请的用户
//		}
		if (requestCode == AtyBrowseUserInfo.REQUEST_BROWSE_USER_INFO) {
			//TODO:Jeffrey 删除被邀请的用户
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyAppList.class);
		context.startActivityForResult(i, REQUEST_APP_LIST);
	}
	
	private class DownloadImageTask extends AsyncTask<String, Integer, Boolean>
    {
		
        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result != null)
            {
            	 Uri uri = Uri.fromFile(new File(savePath + appName + ".apk"));
                 Intent installIntent = new Intent(Intent.ACTION_VIEW);
                 installIntent.setDataAndType(uri,"application/vnd.android.package-archive");
  
                 PendingIntent pi = PendingIntent.getActivity(AtyAppList.this, 0, installIntent, 0);
                 builder.setContentIntent(pi);
                 
                builder.setProgress(0,0,true);
                builder.setContentText("下载完成,点击安装");
                Notification no = builder.build();
                no.flags = Notification.FLAG_AUTO_CANCEL;
                no.defaults = Notification.DEFAULT_SOUND;
                manager.notify(1, no);
                GlobalRes.getInstance().getBeans().setDown(false);
//                isDown = false;
            } else
            {
                builder.setProgress(0, 0, true);
                builder.setContentText("下载失败..");
                manager.notify(1,builder.build());
//                tv_progress.setVisibility(View.GONE);
//                pb.setVisibility(View.GONE);
                Toast.makeText(AtyAppList.this,"下载失败",0).show();
                GlobalRes.getInstance().getBeans().setDown(false);
//                isDown = false;
                
            }
        }
        @Override
        protected Boolean doInBackground(String... params)
        {
            String path = params[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(path);
            
            File file = new File(savePath + appName + ".apk");
            if (file.exists()) {
            	file.delete();
            }
            try {
				file.createNewFile();
			} catch (IOException e1) {
			}
            
            try {  
        		URL url = new URL(path);  
                try {  
                	 HttpResponse resp = client.execute(get);
                     if(resp.getStatusLine().getStatusCode() == 200)
                     {
                         HttpEntity entity = resp.getEntity();
                         if(entity == null)
                         {
                             return null;
                         }
                         long total_length = entity.getContentLength();
                     
                        HttpURLConnection conn = (HttpURLConnection) url  
                                        .openConnection();  
                        InputStream is = conn.getInputStream();  
                        FileOutputStream fos = new FileOutputStream(file);  
                        byte[] buf = new byte[4096];  
                        conn.connect();  
                        int current_len = 0;
                        int len = 0;
                        int progress = 0;//当前下载进度
                        while((len = is.read(buf))!= -1)
                        {
                            current_len+=len;
                            fos.write(buf, 0, len);
                            progress = (int) ((current_len/(float)total_length)*100);
                            builder.setContentText("下载进度："+progress+"%");
                            builder.setProgress(100, progress, false);
                            Notification no = builder.build();
                            no.flags = Notification.FLAG_AUTO_CANCEL;
                            manager.notify(1,no);
                            no = null;
                        }

                        conn.disconnect();  
                        fos.close();  
                        is.close();  
                        return true;
                     }
                } catch (IOException e) {  
            		GlobalRes.getInstance().getBeans().setDown(false);
                    e.printStackTrace();  
                }  
            } catch (MalformedURLException e) {  
            		GlobalRes.getInstance().getBeans().setDown(false);
                    e.printStackTrace();  
            }
            
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values)
        {
//            tv_progress.setText("下载进度:"+values[0]);
        }
        @Override
        protected void onPreExecute()
        {
            builder = new NotificationCompat.Builder(AtyAppList.this);
            builder.setSmallIcon(R.drawable.icon_logo);
            builder.setLargeIcon(bmp);
            builder.setContentTitle(appName);
            PendingIntent pi = PendingIntent.getActivity(AtyAppList.this, 0, new Intent(AtyAppList.this,AtyAppList.class),PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            builder.setProgress(100, 0, false);
            builder.build();
            manager.notify(1,builder.build());
        }
    } 

}
