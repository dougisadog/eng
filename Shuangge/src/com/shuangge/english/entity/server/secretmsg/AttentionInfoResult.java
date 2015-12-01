package com.shuangge.english.entity.server.secretmsg;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;


public class AttentionInfoResult extends RestResult {

	private AttentionData infoData;
	private List<AttentionData> attentions = new ArrayList<AttentionData>();
	private boolean hasNext = false;
	

	public AttentionData getInfoData() {
		return infoData;
	}

	public void setInfoData(AttentionData infoData) {
		this.infoData = infoData;
	}

	public List<AttentionData> getAttentions() {
		return attentions;
	}

	public void setAttentions(List<AttentionData> attentions) {
		this.attentions = attentions;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
}

