package com.shuangge.english.view.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import air.com.shuangge.phone.ShuangEnglish.R;
import air.com.shuangge.phone.ShuangEnglish.wxapi.WXEntryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.network.login.TaskReqLogin;
import com.shuangge.english.network.login.TaskReqOAuth;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.app.ShareManager;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.home.AtyHome;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyLogin extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, TextWatcher {

	public static final int REQUEST_LOGIN = 1050;
	
	private final static int VISITER = 0;
	private final static int LOGIN = 0;
	
	private View bgLoginName, bgPassword;
	private ImageButton btnBack, btnClearLoginName, btnClearPassword;
	private Button btnForgetPwd;
	private RelativeLayout btnLogin, btnRegister;
	private EditText txtLoginName, txtPassword;
	private TextView tip1Login, tip2Login, tip3Login;
	private ImageView imgWX, imgQQ, imgMeten;
	
	private Tencent mTencent;
	private IWXAPI api;
	public static String openidString;
	private static String GET_ACCESS_TOKEN  = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private static String Get_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	private String get_access_token;
	
	private boolean requesting = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_login);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogin = (RelativeLayout) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnRegister = (RelativeLayout) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnForgetPwd = (Button) findViewById(R.id.btnForgetPwd);
		btnForgetPwd.setOnClickListener(this);
		txtLoginName = (EditText) findViewById(R.id.txtLoginName);
		txtLoginName.addTextChangedListener(this);
		txtLoginName.setOnFocusChangeListener(this);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setOnFocusChangeListener(this);
		txtPassword.addTextChangedListener(this);
		
		bgLoginName = findViewById(R.id.bgLoginName);
		bgPassword = findViewById(R.id.bgPassword);
		
		imgWX = (ImageView) findViewById(R.id.imgWX);
		imgWX.setOnClickListener(this);
		imgQQ = (ImageView) findViewById(R.id.imgQQ);
		imgQQ.setOnClickListener(this);
		imgMeten = (ImageView) findViewById(R.id.imgMeten);
		imgMeten.setOnClickListener(this);
		
		tip1Login = (TextView) findViewById(R.id.tip1Login);
		tip2Login = (TextView) findViewById(R.id.tip2Login);
		tip3Login = (TextView) findViewById(R.id.tip3Login);
		
		btnClearLoginName = (ImageButton) findViewById(R.id.btnClearLoginName);
		btnClearLoginName.setOnClickListener(this);
		btnClearPassword = (ImageButton) findViewById(R.id.btnClearPassword);
		btnClearPassword.setOnClickListener(this);
		if (TextUtils.isEmpty(GlobalReqDatas.getInstance().getRequestLoginName())) {
			return;
		}
		txtLoginName.setText(GlobalReqDatas.getInstance().getRequestLoginName());
		checkLoginStatus();
	}

	private boolean validateLogin() {
		if (txtLoginName.getText().toString().length() < 4) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip1), Toast.LENGTH_SHORT).show();
			return false;
		}
		if (txtPassword.getText().toString().length() < 6) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip2), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnBack:
				startActivity(new Intent(this, AtyGuide.class));
				this.finish();
				break;
			case R.id.btnForgetPwd:
				startActivityForResult(new Intent(this, AtyForgetAccount.class), 0);
				break;
			case R.id.btnLogin:
				InputUitls.closeInputMethod(this, txtLoginName, txtPassword);
				if (!validateLogin()) 
					return;
				if (requesting)
					return;
				requesting = true;
				showLoading();
				GlobalReqDatas.getInstance().setRequestLoginName(txtLoginName.getText().toString());
				GlobalReqDatas.getInstance().setRequestPassword(txtPassword.getText().toString());
				new TaskReqLogin(LOGIN, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						hideLoading();
						if (null != result && result) {
							startActivity(new Intent(AtyLogin.this, AtyHome.class));
							AtyLogin.this.finish();
						}
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
				});
				break;
			case R.id.btnRegister:
				startActivity(new Intent(this, AtyRegister.class));
				break;
				
			case R.id.imgWX:
				showLoading(true);
				wxLogin();
//				ShareManager.getInstance().loginWX(this, new CallBackLogin() {
//					
//					@Override
//					public void onError() {
//						hideLoading();
//						Toast.makeText(AtyLogin.this, "登陆失败", Toast.LENGTH_SHORT).show();
//					}
//					
//					@Override
//					public void onComplete() {
//						login();
//					}
//					
//				});
				break;
			case R.id.imgQQ:
				showLoading(true);
				qqLogin();
//				ShareManager.getInstance().loginQQ(this, new CallBackLogin() {
//					
//					@Override
//					public void onError() {
//						hideLoading();
//						Toast.makeText(AtyLogin.this, "登陆失败", Toast.LENGTH_SHORT).show();
//					}
//					
//					@Override
//					public void onComplete() {
//						login();
//					}
//					
//				});
				break;
			case R.id.imgMeten:
				AtyMetenLogin.startAty(AtyLogin.this);
				break;
//			case R.id.btnVistor:
//				GlobalReqDatas.getInstance().setRequestMac(AppInfo.DEVICE_ID);
//				InputUitls.closeInputMethod(this, txtLoginName, txtPassword);
//				if (requesting)
//					return;
//				requesting = true;
//				showLoading();
//				new TaskReqVister(VISITER, new CallbackNoticeView<Void, Boolean>() {
//
//					@Override
//					public void refreshView(int tag, Boolean result) {
//						requesting = false;
//						hideLoading();
//						if (null != result && result) {
//							startActivity(new Intent(AtyLogin.this, AtyHome.class));
//							AtyLogin.this.finish();
//						}
//					}
//
//					@Override
//					public void onProgressUpdate(int tag, Void[] values) {
//					}
//
//				});
//				break;
			case R.id.btnClearLoginName:
				txtLoginName.setText("");
				txtLoginName.requestFocus();
				break;
			case R.id.btnClearPassword:
				txtPassword.setText("");
				txtPassword.requestFocus();
				break;
		}
	}
	
	private void qqLogin() {
		mTencent = Tencent.createInstance(ConfigConstants.QQAPP_ID,getApplicationContext());
		mTencent.login(AtyLogin.this,"all", new BaseUiListener());
	}
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onComplete(Object response) {
//			hideLoading();
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                openidString = ((JSONObject) response).getString("openid");
                Log.e("AtyLogin", "-------------"+openidString);
//                Toast.makeText(getApplicationContext(), "登录成功" + openidString, 0).show();
				GlobalReqDatas.getInstance().setRequestUID(openidString);
                String access_token= ((JSONObject) response).getString("access_token");              
                String expires_in = ((JSONObject) response).getString("expires_in");
				mTencent.setOpenId(openidString);  
                mTencent.setAccessToken(access_token, expires_in);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**到此已经获得OpneID以及其他你想获得的内容了
            QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？ 
            sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息 
                                         如何得到这个UserInfo类呢？  */
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON           
            info.getUserInfo(new IUiListener() {
            	
 
                public void onComplete(final Object response) {
                    // TODO Auto-generated method stub
                    Log.e("AtyLogin", "-----111---"+response.toString());
                    
                    JSONObject json = (JSONObject) response;
                    
                    if (json.has("nickname")) {
	                    try {
	                    	GlobalReqDatas.getInstance().setRequestName(json.getString("nickname"));
//	                    	Toast.makeText(getApplicationContext(),"name" + GlobalReqDatas.getInstance().getRequestName(), 0).show();
	                    } catch (JSONException e) {
	                        e.printStackTrace();
	                    }
                    }
//                    Toast.makeText(getApplicationContext(),"AtyLogin" + GlobalReqDatas.getInstance().getRequestName(), 0).show();
                    GlobalReqDatas.getInstance().setRequestUIDType(2);
                    try {
						GlobalReqDatas.getInstance().setRequestImgHeadUrl(json.getString("figureurl_qq_2"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
                    
                    try {
						if (json.getString("gender").equals("男")) {
							GlobalReqDatas.getInstance().setRequestSex(1);
						} else if (json.getString("gender").equals("女")) {
							GlobalReqDatas.getInstance().setRequestSex(2);
						} else {
							GlobalReqDatas.getInstance().setRequestSex(0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
                    
                    login();
                }               
                
                public void onCancel() {
                }
                
                public void onError(UiError arg0) {
                }
                 
            });
	
		}

		@Override
		public void onError(UiError arg0) {
			Toast.makeText(AtyLogin.this, arg0.errorCode + arg0.errorMessage, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void wxLogin() {
		api = WXAPIFactory.createWXAPI(this, ConfigConstants.WX_APP_ID);
		if( !api.isWXAppInstalled()){
		     Toast.makeText(AtyLogin.this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
		     return;
		}
		api.registerApp(ConfigConstants.WX_APP_ID);
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "shuangge";
		api.sendReq(req);
		hideLoading();
//		Log.e("微信登录", "api.sendReq ");
//		Toast.makeText(AtyLogin.this, "微信登录" +" api.sendReq ", Toast.LENGTH_SHORT).show();
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.e("微信登录", "onResume ");
//		BaseResp resp = GlobalRes.getInstance().getBeans().getWxResp();
//		Toast.makeText(AtyLogin.this, "resp:"+ resp, Toast.LENGTH_SHORT).show();
//		if(resp == null) {
//			return;
//		}
//		Toast.makeText(AtyLogin.this, resp.getType(), Toast.LENGTH_SHORT).show();
//		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//			String weixinCode = ((SendAuth.Resp) resp).code;
//			
//			get_access_token = getTokenRequest(weixinCode);
//			
//			Thread thread=new Thread(downloadRun);
//			thread.start();
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}			
//	}
	
	private void login() {
//		Log.e("微信登录", "login ");
//		Toast.makeText(AtyLogin.this, "微信登录" +" login ", Toast.LENGTH_SHORT).show();
		new TaskReqOAuth(LOGIN, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null != result && result) {
					startActivity(new Intent(AtyLogin.this, AtyHome.class));
					AtyLogin.this.finish();
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		});
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.txtLoginName:
			if (hasFocus) {
				bgLoginName.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtLoginName.getText().toString())) {
				bgLoginName.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		case R.id.txtPassword:
			if (hasFocus) {
				bgPassword.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtPassword.getText().toString())) {
				bgPassword.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		default:
			break;
		}
	}
	
	private void checkLoginStatus() {
		btnClearLoginName.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearPassword.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		tip1Login.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip2Login.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip3Login.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		checkLoginStatus();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Toast.makeText(AtyLogin.this, "微信登录" +" 返回 "+ requestCode, Toast.LENGTH_SHORT).show();
		if (resultCode == CODE_QUIT) {
			this.finish();
		}
		if (mTencent!=null) {
			mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
		}
	}
	
//	private static String getTokenRequest(String code){
//        String tokenRequest = GET_ACCESS_TOKEN.replace("APPID", ConfigConstants.WX_APP_ID).
//                replace("SECRET", ConfigConstants.WX_API_KEY).
//                replace("CODE",code);
//        return tokenRequest;
//	}
//	
//	private static String getUserInfo(String token, String openid){
//		String url = Get_USER_INFO.replace("ACCESS_TOKEN", token).
//				replace("OPENID", openid);
//		return url;
//	}
//	
//	public  Runnable downloadRun = new Runnable() {
//
//		@Override
//		public void run() {
//			WXGetAccessToken();
//			
//		}
//	};
//	
//	private  void WXGetAccessToken(){
//		Log.e("微信登录", "WXGetAccessToken ");
//		Toast.makeText(AtyLogin.this, "WXGetAccessToken ", Toast.LENGTH_SHORT).show();
//		HttpClient get_access_token_httpClient = new DefaultHttpClient();
//		HttpClient get_user_info_httpClient = new DefaultHttpClient();
//		String access_token="";
//		String openid ="";
//		try {
//			HttpPost postMethod = new HttpPost(get_access_token);
//			HttpResponse response = get_access_token_httpClient.execute(postMethod); // 执行POST方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				InputStream is = response.getEntity().getContent();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String str = "";
//				StringBuffer sb = new StringBuffer();
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				is.close();
//				String josn = sb.toString();
//				JSONObject json1 = new JSONObject(josn);
//				access_token = (String) json1.get("access_token");
//				openid = (String) json1.get("openid");
//			
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		String get_user_info_url = getUserInfo(access_token,openid);
//		WXGetUserInfo(get_user_info_url);
//	}
//	
//	private  void WXGetUserInfo(String get_user_info_url){
//		
//		HttpClient get_access_token_httpClient = new DefaultHttpClient();
//		String openid="";
//		String nickname="";
//		String headimgurl="";
//		try {
//			HttpGet getMethod = new HttpGet(get_user_info_url);
//			HttpResponse response = get_access_token_httpClient.execute(getMethod); // 执行GET方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				InputStream is = response.getEntity().getContent();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String str = "";
//				StringBuffer sb = new StringBuffer();
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				is.close();
//				String josn = sb.toString();
//				JSONObject json1 = new JSONObject(josn);
//				openid = (String) json1.get("openid");
//				nickname = (String) json1.get("nickname");
//				headimgurl=(String) json1.get("headimgurl");
//				GlobalReqDatas.getInstance().setRequestName(nickname);
//				GlobalReqDatas.getInstance().setRequestImgHeadUrl(headimgurl);
//				GlobalReqDatas.getInstance().setRequestSex((Integer) json1.get("sex"));
//				GlobalReqDatas.getInstance().setRequestUID(openid);
//				GlobalReqDatas.getInstance().setRequestUIDType(3);
//				login();
//				
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyLogin.class);
		context.startActivityForResult(i, REQUEST_LOGIN);
	}
	
}
