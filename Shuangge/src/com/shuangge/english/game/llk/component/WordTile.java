package com.shuangge.english.game.llk.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.view.component.AutoResizeTextView;

public class WordTile extends RelativeLayout {
	
	private TextView redShadow;
	private AutoResizeTextView txt, translation;
	private ImageView img;
	private RelativeLayout rlTile;
	private int type;
	public final static int ENG = 0 ; //英文 text
	public final static int CN = 1;   // 中文img
	public final static int CN_TRANSLATION = 3;   // 中文img
	public final static int MATCHED = 2;   // 已匹配
	public final static int SIZE = 20; 
	
	private int limitSize = 9;
	
	public WordTile(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.game_llk_word, this);
        rlTile = (RelativeLayout) rootView.findViewById(R.id.rlTile);
        rlTile.setBackgroundColor(Color.WHITE);
        
        txt = (AutoResizeTextView) rootView.findViewById(R.id.txt);
        txt.setMaxTextSize(SIZE);
        txt.setWidthLimit(true);
//        txt.resizeText();
        txt.setVisibility(View.GONE);
//        txt.setMaxEms(limitSize);
        
        translation = (AutoResizeTextView) rootView.findViewById(R.id.translation);
        translation.setMaxTextSize(SIZE);
//        translation.resizeText();
        translation.setVisibility(View.GONE);
//        translation.setEms(limitSize);
        
        img = (ImageView) rootView.findViewById(R.id.img);
        img.setVisibility(View.GONE);
        
        redShadow = (TextView) rootView.findViewById(R.id.redShadow);
        redShadow.setVisibility(View.GONE);
	}
	
	public void reset() {
		txt.setVisibility(View.VISIBLE);
		img.setVisibility(View.GONE);
		redShadow.setVisibility(View.GONE);
		
	}
	
	public void setText(String text) {
		rlTile.setBackgroundColor(Color.WHITE);
		txt.setBackgroundResource(R.drawable.bg_word_cn);
//		text = getTileText(limitSize-1, text);
//		text = getTileText(10, "failsssssssssssssss to oooongzxcassadad");
//		text = "to imiiimasdiamsdis";
		txt.setText(text);
		txt.setMaxLines(text.split(" ").length);
		txt.setVisibility(View.VISIBLE);
		img.setVisibility(View.GONE);
		translation.setVisibility(View.GONE);
		redShadow.setVisibility(View.GONE);
		this.type = ENG;
	}
	
	public void setImgUrl(String imgUrl) {
		rlTile.setBackgroundColor(Color.WHITE);
		img.setImageResource(R.drawable.head_male);
		GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(imgUrl, img));
		img.setVisibility(View.VISIBLE);
		txt.setVisibility(View.GONE);
		translation.setVisibility(View.GONE);
		this.type = CN;
	}
	
	public void setTranslation(String text) {
		rlTile.setBackgroundColor(Color.WHITE);
		translation.setBackgroundResource(R.drawable.bg_word_eng);
		translation.setText(text);
		translation.setVisibility(View.VISIBLE);
		img.setVisibility(View.GONE);
		txt.setVisibility(View.GONE);
		redShadow.setVisibility(View.GONE);
		this.type = CN_TRANSLATION;
	}
	
	public void setSelected(boolean selected) {
		if (type == CN || type == CN_TRANSLATION) {
		if (!selected) {
			rlTile.setBackgroundColor(Color.WHITE);
			return;
		}
		rlTile.setBackgroundColor(Color.YELLOW);
		}
		else if (type == ENG){
			if (!selected) {
				rlTile.setBackgroundColor(Color.WHITE);
//				rlTile.setBackgroundResource(R.drawable.bg_word_eng);
				return;
			}
			rlTile.setBackgroundColor(Color.YELLOW);
//			rlTile.setBackgroundResource(R.drawable.bg_word_eng_clicked);
		}
	}
	
	public void setMatched() {
		if (type == CN) {
			translation.setVisibility(View.GONE);
			txt.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			img.setImageResource(R.drawable.bg_word_matched);
		}
		else if (type == ENG) {
			translation.setVisibility(View.GONE);
			txt.setVisibility(View.VISIBLE);
			txt.setText("");
			img.setVisibility(View.GONE);
			txt.setBackgroundResource(R.drawable.bg_word_matched);
		}
		else if (type == CN_TRANSLATION) {
			translation.setVisibility(View.VISIBLE);
			txt.setVisibility(View.GONE);
			img.setVisibility(View.GONE);
			translation.setText("");
			translation.setBackgroundResource(R.drawable.bg_word_matched);
		}
		rlTile.setBackgroundColor(Color.WHITE);
		type = MATCHED;
	}
	
	public void setError() {
		redShadow.setVisibility(View.VISIBLE);
//		redShadow.setBackgroundColor(Color.parseColor("#33FF0000"));
		if (type == CN) {
			translation.setVisibility(View.GONE);
			txt.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
//			img.setImageResource(R.drawable.bg_word_err);
		}
		else if (type == ENG) {
			translation.setVisibility(View.GONE);
			txt.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
		}
		else if (type == CN_TRANSLATION) {
			translation.setVisibility(View.VISIBLE);
			txt.setVisibility(View.GONE);
			img.setVisibility(View.GONE);
		}
		rlTile.setVisibility(View.VISIBLE);
		AlphaAnimation animation = new AlphaAnimation((float) 0.6, 0);
		animation.setFillAfter(true);
		animation.setDuration(500);
		redShadow.startAnimation(animation);
		rlTile.setBackgroundColor(Color.WHITE);
	}

	public int getType() {
		return type;
	}

	//设置 所属类型背景类型
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * 根据空格和 ‘-’ 拆分字符串 单段 长度超越 限制长度 则 增加‘-’并强制换行 其他换行由textview 本身的换行机制执行
	 * @param maxLength  单行限制最多字符数 根据veiw的宽度 和 textSize 来手动设置
	 * @param text  需要转换的文本
	 * return 处理后用于显示的文本
	 */
	public static String getTileText(int maxLength, String text) {
		if (text.length() <= maxLength) return text;
		String sp1 = " ";
		String sp2 = "-";
		
		String[] sText1;
		String[] sText2;
		StringBuffer result = new StringBuffer();
		sText1 = text.split(sp1);
		for (int i = 0; i < sText1.length; i++) {
			if (i > 0) result.append(sp1);
				sText2 = sText1[i].split(sp2);
				for (int j = 0; j < sText2.length; j++) {
					if (j > 0) result.append(sp2);
					if (sText2[j].length() > maxLength) {
					result.append(sText2[j].substring(0, maxLength) + sp2 + "\n" + sText2[j].substring(maxLength, sText2[j].length()-1));
					}
					else {
						result.append(sText2[j]);
					}
				}
		}
		return result.toString();
	}
	
}


