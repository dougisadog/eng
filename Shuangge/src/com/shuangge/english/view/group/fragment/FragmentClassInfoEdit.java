package com.shuangge.english.view.group.fragment;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.network.group.TaskReqUpdateClassInfo;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.group.UpdateClassData;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment.CallBackDialogConfirmSave;
import com.shuangge.english.view.component.drag.DragGridView.OnDragListener;
import com.shuangge.english.view.component.photograph.MyScrollView;
import com.shuangge.english.view.component.photograph.PhotosEditContainer;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment.CallBackDialogCitiesWheel;
import com.shuangge.english.view.group.AtyClassSettings;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentClassInfoEdit extends BaseClassFragment implements OnClickListener, OnFocusChangeListener, OnDragListener, TextWatcher {

	public static final int MODULE_1 = 1;
	
	private boolean requesting = false;
	
	private RelativeLayout rlSave;
	
	private PhotosEditContainer photosEditContainer;
	private MyScrollView sv;
	
	private RelativeLayout rl1, rl2, rl3, rl4,rl5;
	private EditText et1, et2, et4, et5;
	private TextView txt3;
	private RadioGroup raidoGroup;
	private RadioButton rb1, rb2, rb3;
	private Integer joinRule;
	
	private DialogConfirmSaveFragment dialogFragment;
	private DialogCitiesFragment dialogWheel;
	private boolean dataHasChanged = false;
	
	public FragmentClassInfoEdit() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_classinfo_edit, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		
		rlSave = (RelativeLayout) mainView.findViewById(R.id.rlSave);
		rlSave.setOnClickListener(this);
		
		sv = (MyScrollView) mainView.findViewById(R.id.sv);
		photosEditContainer = (PhotosEditContainer) mainView.findViewById(R.id.photosEditContainer);
		photosEditContainer.setDragListener(this);
		
		rl1 = (RelativeLayout) mainView.findViewById(R.id.rl1);
		rl1.setOnClickListener(this);
		rl2 = (RelativeLayout) mainView.findViewById(R.id.rl2);
		rl2.setOnClickListener(this);
		rl3 = (RelativeLayout) mainView.findViewById(R.id.rl3);
		rl3.setOnClickListener(this);
		rl4 = (RelativeLayout) mainView.findViewById(R.id.rl4);
		rl4.setOnClickListener(this);
		rl5 = (RelativeLayout) mainView.findViewById(R.id.rl5);
		rl5.setOnClickListener(this); 
		
		txt3 = (TextView) mainView.findViewById(R.id.classEditTip3);
		et1 = (EditText) mainView.findViewById(R.id.classEditTip1);
		et1.setOnFocusChangeListener(this);
		et2 = (EditText) mainView.findViewById(R.id.classEditTip2);
		et2.setOnFocusChangeListener(this);
		et4 = (EditText) mainView.findViewById(R.id.classEditTip4);
		et4.setOnFocusChangeListener(this);
		et4.addTextChangedListener(this);
		et5 = (EditText) mainView.findViewById(R.id.classEditTip5);
		et5.setOnFocusChangeListener(this);
		
		raidoGroup = (RadioGroup) mainView.findViewById(R.id.radioGroup);
		rb1 = (RadioButton) mainView.findViewById(R.id.approval);
		rb2 = (RadioButton) mainView.findViewById(R.id.notApproval);
		rb3 = (RadioButton) mainView.findViewById(R.id.approvalWithWx);
		
		raidoGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) mainView.findViewById(radioButtonId);
				joinRule = Integer.valueOf(rb.getTag().toString());
			}
		});
		
		bindingData();
		
		return mainView;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		photosEditContainer.recycle();
	}
	
	private void bindingData() {
		if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() == 0) {
			return;
		}
		ClassData data = GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0);
		txt3.setText(data.getLocation());
		et1.setText(data.getName());
		et2.setText(data.getSignature());
		et4.setText(data.getDescription());
		et5.setText(data.getWechatNo());
		photosEditContainer.setUrls(data.getPhotoUrls(),
				data.getPhotoNos(),
				data.getPhotoSortNos());
		if (rb1.getTag().equals(data.getJoinRule().toString())) {
			raidoGroup.check(rb1.getId());
		}
		else if (rb2.getTag().equals(data.getJoinRule().toString())) {
			raidoGroup.check(rb2.getId());
		}
		else {
			raidoGroup.check(rb3.getId());
		}
		
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		UpdateClassData data = GlobalReqDatas.getInstance().getUpdateClassData();
		data.setLocation(txt3.getText().toString());
		data.setSignature(et2.getText().toString());
		data.setName(et1.getText().toString());
		data.setDescription(et4.getText().toString());
		data.setWechatNo(et5.getText().toString());
		data.setJoinRule(joinRule);
		data.clear();
		data.setPhotoDatas(photosEditContainer.getDatas());
		
		showLoading();
		new TaskReqUpdateClassInfo(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				requesting = false;
				if (null == result || !result) {
					return;
				}
				dataHasChanged = false;
				Toast.makeText(getActivity(), R.string.saveSuccessTipCn, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.rlSave:
				requestData();
				break;
			case R.id.rl3:
				dataHasChanged = true;
				dialogWheel = new DialogCitiesFragment(callback, getString(R.string.userInfoEditDialogTip9), txt3.getText().toString());
				dialogWheel.showDialog(getActivity().getSupportFragmentManager());
				break;
			case R.id.rl1:
				dataHasChanged = true;
				et1.requestFocus();
				break;
			case R.id.rl2:
				dataHasChanged = true;
				et2.requestFocus();
				break;
			case R.id.rl4:
				dataHasChanged = true;
				et4.requestFocus();
				break;
			case R.id.rl5:
				dataHasChanged = true;
				et5.requestFocus();
				break;
		}
	}
	
	private CallBackDialogCitiesWheel callback = new CallBackDialogCitiesWheel() {
		
		@Override
		public void submit(String data) {
			dataHasChanged = true;
			txt3.setText(data);
			cancel();
		}
		
		@Override
		public void cancel() {
			if (null != dialogWheel) {
				dialogWheel.dismissAllowingStateLoss();
			}
			dialogWheel = null;
		}
		
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PhotosEditContainer.MODULE_PHOTO:
			dataHasChanged = true;
			photosEditContainer.onActivityResult(resultCode, data);
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			dataHasChanged = true;
			if (v instanceof EditText) {
				((EditText) v).setSelection(((EditText) v).getText().length());
			}
			return;
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		et4.setGravity(et4.getLineCount() > 1 ? Gravity.LEFT : Gravity.CENTER);
	}
	
	@Override
	public void onDrag() {
		((AtyClassSettings)getActivity()).onDrag();
		sv.setCanScroll(false);
	}

	@Override
	public void onStopDrag() {
		((AtyClassSettings)getActivity()).onStopDrag();
		sv.setCanScroll(true);
	}
	
	@Override
    public boolean onBack() {
    	return gotoBack();
    }
	
	@Override
	public boolean onFinish() {
		return gotoBack();
	}
	
	private boolean gotoBack() {
		if (!dataHasChanged || null != dialogFragment) {
			return false;
		}
		dialogFragment = new DialogConfirmSaveFragment(new CallBackDialogConfirmSave() {
			
			@Override
			public void save() {
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
				requestData();
			}
			
			@Override
			public void noSave() {
				dataHasChanged = false;  
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
			}
			
			@Override
			public void cancel() {
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
			}
			
		});
		dialogFragment.showDialog(getActivity().getSupportFragmentManager());
		return true;
	}

}
