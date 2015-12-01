package com.shuangge.english.view.read.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shuangge.english.view.lesson.adapter.AdapterFragmentPager;
import com.shuangge.english.view.read.fragment.FragmentVideoTest;


public class AdapterVideoTest extends AdapterFragmentPager {
	
	private List<FragmentVideoTest> datas = new ArrayList<FragmentVideoTest>();

	public AdapterVideoTest(FragmentManager fm) {
		super(fm);
	}

	@Override
	public FragmentVideoTest getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	public void setDatas(FragmentManager fm, List<FragmentVideoTest> datas) {
		initialized = false;
        if (null != this.datas && this.datas.size() > 0) {  
            FragmentTransaction ft = fm.beginTransaction();
            for (FragmentVideoTest f : this.datas) {
//            	if (!f.isAdded()) {
//            		continue;
//            	}
                ft.remove(f);  
            }  
            ft.commitAllowingStateLoss();  
            ft = null;  
            fm.executePendingTransactions();  
        }  
        this.datas = datas;  
        notifyDataSetChanged();  
    }

	public List<FragmentVideoTest> getDatas() {
		return datas;
	}

}
