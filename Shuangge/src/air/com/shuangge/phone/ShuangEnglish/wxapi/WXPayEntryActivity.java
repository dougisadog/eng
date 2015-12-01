package air.com.shuangge.phone.ShuangEnglish.wxapi;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.shop.CashPayData;
import com.shuangge.english.network.shop.TaskReqOrderBuyWx;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.shuangge.english.view.shop.AtyShopOrderDetails;
import com.shuangge.english.view.shop.AtyShopOrderPay;
import com.shuangge.english.view.shop.GoodsFunc;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AbstractAppActivity implements IWXAPIEventHandler{
	
	public static final Integer SUCCESS_STATUS = 0;
	public static final Integer UNCHECKED_STATUS = 1;
	public static final Integer FAIL_STATUS = 2;
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private static final int RESULTCODE = 1<<2;
	
    private IWXAPI api;
    private DialogAlertFragment successDialog;
    private Integer code;
    
    private static String orderNo;
    private static int amount = 0;
    
    public static void setWXPayData(String orderNo, int amount) {
    	WXPayEntryActivity.orderNo = orderNo;
    	WXPayEntryActivity.amount = amount;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result); 
    	api = WXAPIFactory.createWXAPI(this, ConfigConstants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		AtyShopOrderPay.paying = false;  //给微信支付的结果码加上的一个标识,防止 微信支付和支付宝支付 重复点击 和 支付失败设置点击事件恢复正常 
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			code = resp.errCode;
			//非成功不处理
//			Toast.makeText(WXPayEntryActivity.this, "微信支付返回" +code, Toast.LENGTH_SHORT).show();
			if (code != 0) {
				finish();
				return;
			}
			requestPayData();
		}
	}
	
	private void requestPayData() {
		
		showLoading();
		List<String> orderNos = new ArrayList<String>();
		orderNos.add(orderNo);
		List<Integer> errCodes = new ArrayList<Integer>();
		errCodes.add(code);
		new TaskReqOrderBuyWx(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null != result && result) {
					List<CashPayData> cashPayDatas = GlobalRes.getInstance().getBeans().getPayResult().getCashPayDatas();
//					Toast.makeText(WXPayEntryActivity.this, "cashPayDatas.size()="+ cashPayDatas.size() + "cashPayDatas.get(0).getCode()="+cashPayDatas.get(0).getCode(), Toast.LENGTH_SHORT).show();
					if (cashPayDatas != null && cashPayDatas.size() > 0 && cashPayDatas.get(0).getCode() == SUCCESS_STATUS) {
						finish();
						requestLessonData(cashPayDatas.get(0).getFunc());
//						GlobalRes.getInstance().getBeans().setCurrentOrderId(GlobalRes.getInstance().getBeans().getOrderSimpleResult().getOrderData().getOrderNo());
//						AtyShopOrderDetails.startAty(WXPayEntryActivity.this);
						
						return;
					}
				}
//				Toast.makeText(WXPayEntryActivity.this, "result:"+ result, Toast.LENGTH_SHORT).show();
				successDialog = new DialogAlertFragment(callback2, "服务器验证失败，点击确定重新验证", "", 0);
				successDialog.showDialog(getSupportFragmentManager());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, orderNos, errCodes);
	}
	
	private void requestLessonData(int func) {
		if (func == 100) {
			int num = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getKeyNum() + amount;
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setKeyNum(num);
			GlobalRes.getInstance().getBeans().setCurrentOrderId(orderNo);
			AtyShopOrderDetails.startAty(WXPayEntryActivity.this);
			researchAty();
			return;
		}
		else if (func > 0 && func < 100) {
			GlobalRes.getInstance().getBeans().setCurrentOrderId(orderNo);
			GoodsFunc goodsFunc = new GoodsFunc(func, new GoodsFunc.CallBackFunc() {
				
				@Override
				public void onCallBack() {
					GlobalRes.getInstance().getBeans().setCurrentOrderId(orderNo);
					AtyShopOrderDetails.startAty(WXPayEntryActivity.this);
//					Toast.makeText(WXPayEntryActivity.this,"隐藏aty", Toast.LENGTH_SHORT).show();
					researchAty();
				}
			
			});
			return;
		}
		
//		Toast.makeText(WXPayEntryActivity.this, String.valueOf(func), Toast.LENGTH_SHORT).show();
		
		GlobalRes.getInstance().getBeans().setCurrentOrderId(orderNo);
		AtyShopOrderDetails.startAty(WXPayEntryActivity.this);
		researchAty();
		
	}
	
	private void researchAty() {
		List<Activity> atyList = GlobalApp.getInstance().getStackActivities();
		for (int i = 0; i < atyList.size(); i++) {
			if (atyList.get(i).getLocalClassName().toString().equals("com.shuangge.english.view.shop.AtyShopOrderPay")) {
				atyList.get(i).finish();
			}
		}
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			requestPayData();
			successDialog.dismiss();
			successDialog = null;
		}
		
		@Override
		public void onKeyBack() {
			successDialog.dismiss();
			successDialog = null;
		}
		
	};
}