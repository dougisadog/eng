package com.shuangge.english.view.component.viewpaper;

import android.view.View;

public interface ViewPaperIndicator {

	void onSwitched(View view, int position);

	public void setViewPager(HackyViewPager view);

	public void onScrolled(int h, int v, int oldh, int oldv);

}
