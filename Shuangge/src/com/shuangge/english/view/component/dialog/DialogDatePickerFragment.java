package com.shuangge.english.view.component.dialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.widget.DatePicker;
import android.widget.Toast;

import com.shuangge.english.support.utils.DateUtils;

public class DialogDatePickerFragment extends DialogFragment1 implements DatePickerDialog.OnDateSetListener {
	
	public static interface CallBackDialogDatePicker {
		
		public void submit(int tag, Integer data, Date date);
		
	}
	
	private int tag;
	private CallBackDialogDatePicker callback;
	private Date date;
	
	public DialogDatePickerFragment() {
		super();
	}
	
	public DialogDatePickerFragment(int tag, Date date, CallBackDialogDatePicker callback) {
		super();
		this.tag = tag;
		this.date = date;
		this.callback = callback;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance(Locale.CHINA);
		if (null != date) {
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
		}
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		return new MyDatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Date date = null;
		Integer age = null;
		try {
			date = DateUtils.getDateFmt().parse(year + "-" + monthOfYear + "-" + dayOfMonth);
			age = Integer.parseInt(DateUtils.getAge(date) + "");
			if (age <= 0 || age > 100) {
				Toast.makeText(getActivity(), R.string.ageErrTip, Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), R.string.ageErrTip, Toast.LENGTH_SHORT).show();
			return;
		}
		callback.submit(tag, age, date);
	}
	
	public static class MyDatePickerDialog extends DatePickerDialog {

		public MyDatePickerDialog(Context context,
				OnDateSetListener listener, int year, int monthOfYear,
				int dayOfMonth) {
			super(context, 0, listener, year, monthOfYear, dayOfMonth);
		}
		
		@Override
		protected void onStop() {
//			super.onStop();
		}
		
	}
	
}
