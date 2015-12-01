package com.shuangge.english.support.app;

public class ShareContentWebPage {
	
	/**
     * 文字
     */
    public static final int WEIXIN_SHARE_WAY_TEXT = 1;
    /**
     * 图片
     */
    public static final int WEIXIN_SHARE_WAY_PIC = 2;
    /**
     * 链接
     */
	public static final int WEIXIN_SHARE_WAY_WEBPAGE = 3;
	
	private String title;
    private String content;
    private String url;
    private String picUrl;
    public ShareContentWebPage() {
    	
    }
    public ShareContentWebPage(String title, String content, 
            String url, String picUrl){
        this.title = title;
        this.content = content;
        this.url = url;
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getShareWay() {
        return WEIXIN_SHARE_WAY_WEBPAGE;
    }
	public void setContent(String content) {
		this.content = content;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setURL(String url) {
		this.url = url;
	}
	public void setPicUrl(String url) {
		this.picUrl = picUrl;
	}

}
