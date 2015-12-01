package com.shuangge.english.view.read.component;

import android.text.style.ClickableSpan;
import android.view.View;

public class WordClickableSpan extends ClickableSpan {

	private String word;
	
	public WordClickableSpan() {
	}

	public WordClickableSpan(String word) {
		this.word = word;
	}

	@Override
	public void onClick(View widget) {
	}

}
