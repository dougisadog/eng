package com.shuangge.english.view.lesson.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.lesson.component.OptionImg;

public class AdapterBoss extends BaseAdapter {
	
    private LayoutInflater mInflater;
    private List<LessonData> datas = new ArrayList<LessonData>();
    private SparseIntArray distanceMap = new SparseIntArray();
    private SparseIntArray stepMap = new SparseIntArray();
    private SparseIntArray pageMap = new SparseIntArray();
    private int margin;
    private int marginBig;
    private int imgWidth;
    private int imgBigWidth;
    private int distance1, distance2;
    private int firstSize = 0;

    private List<OptionImg> imgs = new ArrayList<OptionImg>();
    
    public AdapterBoss(Context context, List<List<LessonFragment>> datas) {
    	super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        margin = DensityUtils.dip2px(context, 10);
//        imgWidth = (int) ((AppInfo.getScreenWidth() - margin * 4) / 2.5);
        imgWidth = (int) ((AppInfo.getScreenWidth() - margin * 6) / 2.5);
        imgBigWidth = (int) (imgWidth * 1.5);
//        marginBig = (int) (margin + (imgWidth * 0.5 / 6));
        marginBig = (int) (margin + (imgWidth * 0.5 / 6));
//        marginBig = margin;
        
        //一张图片距离
        distance1 = (int) (imgWidth + marginBig * 2);
        //一半图片距离
        distance2 = distance1 / 2;
        int step = 1;
        int pageNo = 0;
        int prevSize = 0;
        int currSize = 0;
        int distance = 0;
        this.datas.add(null);
        for (List<LessonFragment> list : datas) {
        	stepMap.put(pageNo, -1);
        	for (LessonFragment lessonFragment : list) {
        		this.datas.addAll(lessonFragment.getDatas());
        		currSize = lessonFragment.getDatas().size();
        		
        		if (stepMap.get(pageNo) == -1) {
    				stepMap.put(pageNo, step);
    				pageMap.put(step, pageNo);
    			}
        		
        		//第一次进来
        		if (firstSize == 0) {
        			firstSize = currSize;
        		}
    			if (prevSize == 0) {
        			if (currSize == 1) {
        				distanceMap.put(step++, distance);
        			}
        			else {
        				distanceMap.put(step++, distance);
        				distanceMap.put(step++, distance);
        			}
    			}
    			else if (prevSize == 1) {
					if (currSize == 1) {
						distance += distance1;
						distanceMap.put(step++, distance);
        			}
        			else {
        				distance += distance1 + distance2;
        				distanceMap.put(step++, distance);
        				distanceMap.put(step++, distance);
        			}
    			}
    			else {
					if (currSize == 1) {
						distance += distance1 * 2 - distance2;
						distanceMap.put(step++, distance);
        			}
        			else {
        				distance += distance1 * 2;
        				distanceMap.put(step++, distance);
        				distanceMap.put(step++, distance);
        			}
    			}
    			prevSize = currSize;
			}
        	++pageNo;
		}
        this.datas.add(null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
    	long time = System.currentTimeMillis();
        if (null == convertView) {
        	 convertView = mInflater.inflate(R.layout.lesson_boss, parent, false);
             holder = new ViewHolder();
             holder.llImgsContainer = (LinearLayout) convertView.findViewById(R.id.llImgsContainer);
             convertView.setTag(holder);
            
            int w = imgBigWidth, h = imgBigWidth;
    		Bitmap bitmap = null;
    		OptionImg img = null;
    		img = new OptionImg(holder.llImgsContainer.getContext(), R.drawable.bg_head, bitmap, w, h);
    		ViewUtils.setLinearMargins(img, imgWidth, imgWidth, marginBig, 0, marginBig, 0);
    		holder.llImgsContainer.addView(img);
    		holder.img = img;
    		convertView.setTag(holder);
    		imgs.add(img);
        } 
        else {
            holder = (ViewHolder) convertView.getTag();
        }
//        DebugPrinter.i("耗时1：" + (System.currentTimeMillis() - time) + "ms");
        refreshCurrentView(getItem(position), holder, position);
//        DebugPrinter.i("耗时all：" + (System.currentTimeMillis() - time) + "ms");
        return convertView;
    }

    private static class ViewHolder {
        
    	private LinearLayout llImgsContainer;
    	private OptionImg img;
    	
    }
    
    private void refreshCurrentView(LessonData data, ViewHolder holder, int position) {
    	if (position == 0) {
    		holder.img.setVisibility(View.INVISIBLE);
    		if (firstSize == 2) {
    			ViewUtils.setLinearMargins(holder.llImgsContainer, marginBig, LinearLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
    		}
    		else {
    			ViewUtils.setLinearMargins(holder.llImgsContainer, ((AppInfo.getScreenWidth() - imgWidth) >> 1) - marginBig, LinearLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
    		}
    		return;
    	}
    	if (position == getCount() - 1) {
    		holder.img.setVisibility(View.INVISIBLE);
    		ViewUtils.setLinearMargins(holder.llImgsContainer, ((AppInfo.getScreenWidth() - imgWidth) >> 1), LinearLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
    		return;
    	}
		ViewUtils.setLinearMargins(holder.llImgsContainer, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
		holder.img.setVisibility(View.VISIBLE);
    	if (null == data)  {
    		return;
    	}
////    	//60 60 100 45
////    	int h = AppInfo.getScreenHeight() - DensityUtils.dip2px(holder.llImgsContainer.getContext(), 60 + 60 + 100 + 45);
		String path = data.getAnswer().getLocalImgPath();
		holder.img.clear();
		holder.img.setWrong();
		GlobalResTypes.getInstance().displayLessonBitmapNoCache(path, holder.img);
	}
    
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public LessonData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getDistanceByStep(int step) {
		return distanceMap.get(step, -1);
	}
	
	public int getStepByPageNo(int pageNo) {
		return stepMap.get(pageNo);
	}
	
	public int getPageNoByStep(int step) {
		return pageMap.get(step);
	}

	public void recycle() {
		for (OptionImg img : imgs) {
			img.recycle();
		}
		imgs = null;
	}
	
}
