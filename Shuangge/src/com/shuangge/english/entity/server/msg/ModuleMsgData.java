package com.shuangge.english.entity.server.msg;


public class ModuleMsgData {

	private boolean attentionMsg = false;
	private boolean classMsg = false;
	private boolean postMsg = false;
	private boolean systemMsg = false;
	private int secretMsg = 0;
	private Integer giftMsg = 0;
	
	private Long attentionTimestamp = 0L;
	private Long classTimestamp = 0L;
	private Long postTimestamp = 0L;
	private Long systemTimestamp = 0L;
	private Long secretTimestamp = 0L;
	private Long giftMsgTimestamp = 0L;
	
	public boolean isAttentionMsg() {
		return attentionMsg;
	}

	public void setAttentionMsg(boolean attentionMsg) {
		this.attentionMsg = attentionMsg;
	}

	public boolean isClassMsg() {
		return classMsg;
	}

	public void setClassMsg(boolean classMsg) {
		this.classMsg = classMsg;
	}

	public boolean isPostMsg() {
		return postMsg;
	}

	public void setPostMsg(boolean postMsg) {
		this.postMsg = postMsg;
	}

	public boolean isSystemMsg() {
		return systemMsg;
	}

	public void setSystemMsg(boolean systemMsg) {
		this.systemMsg = systemMsg;
	}

	public Long getAttentionTimestamp() {
		return attentionTimestamp;
	}

	public void setAttentionTimestamp(Long attentionTimestamp) {
		this.attentionTimestamp = attentionTimestamp;
	}

	public Long getClassTimestamp() {
		return classTimestamp;
	}

	public void setClassTimestamp(Long classTimestamp) {
		this.classTimestamp = classTimestamp;
	}

	public Long getPostTimestamp() {
		return postTimestamp;
	}

	public void setPostTimestamp(Long postTimestamp) {
		this.postTimestamp = postTimestamp;
	}

	public Long getSystemTimestamp() {
		return systemTimestamp;
	}

	public void setSystemTimestamp(Long systemTimestamp) {
		this.systemTimestamp = systemTimestamp;
	}

	public Long getSecretTimestamp() {
		return secretTimestamp;
	}

	public void setSecretTimestamp(Long secretTimestamp) {
		this.secretTimestamp = secretTimestamp;
	}

	public int getSecretMsg() {
		return secretMsg;
	}

	public void setSecretMsg(int secretMsg) {
		this.secretMsg = secretMsg;
	}

	public Integer getGiftMsg() {
		return giftMsg;
	}

	public void setGiftMsg(Integer giftMsg) {
		this.giftMsg = giftMsg;
	}

	public Long getGiftMsgTimestamp() {
		return giftMsgTimestamp;
	}

	public void setGiftMsgTimestamp(Long giftMsgTimestamp) {
		this.giftMsgTimestamp = giftMsgTimestamp;
	}

}

