package com.shuangge.english.view.read.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shuangge.english.view.lesson.adapter.AdapterFragmentPager;
import com.shuangge.english.view.read.fragment.BaseLessonType;


public class AdapterReadLesson extends AdapterFragmentPager {
	
	private List<BaseLessonType> datas = new ArrayList<BaseLessonType>();

	public AdapterReadLesson(FragmentManager fm) {
		super(fm);
	}

	@Override
	public BaseLessonType getItem(int arg0) {
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

	public void setDatas(FragmentManager fm, List<BaseLessonType> datas) {
		initialized = false;
        if (null != this.datas && this.datas.size() > 0) {  
            FragmentTransaction ft = fm.beginTransaction();
            for (BaseLessonType f : this.datas) {
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

	public List<BaseLessonType> getDatas() {
		return datas;
	}
	
		//1.带选项的 随机顺序
//		2.正确后  播放正确答案
		
		//01	1.只播 2秒  跟音频时间走  ABCD			2  没选项
			
		//02	1.有声音播一遍  2.读 3.错 继续(3次) ABC	2 语音
			
		//03	1.文字按钮选择  AB 2-4个选项 选项为文章		2 文字选择
		//07	1.填空 选择	A (空:___) 正确之后 下划线变成正确答案 会读取blanks.txt(有二次选择) 比较特殊  选项为文章 2-4个选项	2  文字选择
		
		//10	A 写作 writeblanks.txt				2 写作
		//11	A 写作							2 写作
			
		//08	1.选择题 A 选项2-3  选项为图片 			选择图片
		//05	1.必须4题 并且互为选项 选项为图片 4个选项
		//06	等于05C 4个选项
		//13	ABC	类似05 配置2-4个选项 选项为图片
			
		//14	ABC	类似13 必须2个选项 选项为图片()
	

}
