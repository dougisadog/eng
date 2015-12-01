package com.shuangge.english.view.component;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuangge.english.support.utils.ViewUtils;

public class RatingBarView extends LinearLayout {
    private List<ImageView> mStars = new ArrayList<ImageView>();
    private boolean mClickable = false;
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;
    private int starNum;
    private int starCount;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private float starSpacing = 5;

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    private int startCount;


    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        starImageSize = a.getDimension(R.styleable.RatingBarView_starImageSize, 20);
        starNum = a.getInteger(R.styleable.RatingBarView_starNum, 5);
        starCount = a.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyDrawable = a.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = a.getDrawable(R.styleable.RatingBarView_starFill);
        starSpacing = a.getDimension(R.styleable.RatingBarView_starSpacing, 5);

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, attrs, i == 0 ? 0 : (int) starSpacing);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable){
                        setStar(indexOfChild(v) + 1);
                        if (onRatingListener != null) {
                            onRatingListener.onRating(bindObject,indexOfChild(v) + 1);
                        }
                    }

                }
            });
            addView(imageView);
        }
        setStar(starNum);
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs, int spacing) {
        ImageView imageView = new ImageView(context);
        ViewUtils.setLinearMargins(imageView, Math.round(starImageSize), Math.round(starImageSize), spacing, 0, 0, 0);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        return imageView;

    }
    
    public void setStar(int starCount) {
        setStar(starCount, true);
    }

    public void setStar(int starCount,boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        //TODO
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
//            if(animation) YoYo.with(Techniques.BounceIn).duration(400).playOn(getChildAt(i));
        }

        for (int i = this.starCount-1 ; i >= starCount; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }

    }


    /**
     * 该监听器用于监听选中Tab时View的变化
     */
    public interface OnRatingListener {

        void onRating(Object bindObject,int RatingScore);

    }
}
