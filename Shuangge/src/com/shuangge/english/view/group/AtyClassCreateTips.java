package com.shuangge.english.view.group;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassCreateTips extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_CREATE_CLASS_TIPS = 1016;
	
	public static final int CODE_CLOSE = 1;

	private ImageButton btnBack;
	private TextView txtSubmit;
	private ImageView imgHelp1, imgHelp2, imgHelp3;
	private LinearLayout llMeetTheConditions1, llMeetTheConditions2, llMeetTheConditions3;
	private RelativeLayout rlNotMeetTheConditions1, rlNotMeetTheConditions2, rlNotMeetTheConditions3;
	
	private boolean canCreate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_class_create_tips);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		imgHelp1 = (ImageView) findViewById(R.id.imgHelp1);
		imgHelp1.setOnClickListener(this);
		imgHelp2 = (ImageView) findViewById(R.id.imgHelp2);
		imgHelp2.setOnClickListener(this);
		imgHelp3 = (ImageView) findViewById(R.id.imgHelp3);
		imgHelp3.setOnClickListener(this);
		
		llMeetTheConditions1 = (LinearLayout) findViewById(R.id.llMeetTheConditions1);
		llMeetTheConditions2 = (LinearLayout) findViewById(R.id.llMeetTheConditions2);
		llMeetTheConditions3 = (LinearLayout) findViewById(R.id.llMeetTheConditions3);
		
		rlNotMeetTheConditions1 = (RelativeLayout) findViewById(R.id.rlNotMeetTheConditions1);
		rlNotMeetTheConditions2 = (RelativeLayout) findViewById(R.id.rlNotMeetTheConditions2);
		rlNotMeetTheConditions3 = (RelativeLayout) findViewById(R.id.rlNotMeetTheConditions3);
		
		InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
//		boolean conditions1 = data.getHonorData().getLevel() >= 1;
		boolean conditions2 = !data.isVisitor();
		boolean conditions3 = GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() == 0;
		
//		llMeetTheConditions1.setVisibility(conditions1 ? View.VISIBLE : View.GONE);
//		rlNotMeetTheConditions1.setVisibility(!conditions1 ? View.VISIBLE : View.GONE);
		
		llMeetTheConditions2.setVisibility(conditions2 ? View.VISIBLE : View.GONE);
		rlNotMeetTheConditions2.setVisibility(!conditions2 ? View.VISIBLE : View.GONE);
		
		llMeetTheConditions3.setVisibility(conditions3 ? View.VISIBLE : View.GONE);
		rlNotMeetTheConditions3.setVisibility(!conditions3 ? View.VISIBLE : View.GONE);
		
		canCreate = conditions2 && conditions3;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtSubmit:
			if (!canCreate) {
				Toast.makeText(AtyClassCreateTips.this, R.string.bindingAccountErrTip, Toast.LENGTH_LONG).show();
				startActivityForResult(new Intent(AtyClassCreateTips.this, AtyBindingAccount.class), 0);
			}
			else {
				startActivityForResult(new Intent(AtyClassCreateTips.this, AtyClassCreate.class), 0);
			}
			break;
		case R.id.imgHelp1:
			break;
		case R.id.imgHelp2:
			break;
		case R.id.imgHelp3:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AtyClassCreateTips.CODE_QUIT) {
			setResult(AtyClassRecommend.CODE_QUIT);
			this.finish();
		}
		else if(resultCode == AtyBindingAccount.CODE_BINDING_ACCOUNT) {
			startActivityForResult(new Intent(AtyClassCreateTips.this, AtyClassCreate.class), 0);
		}
	}

}
