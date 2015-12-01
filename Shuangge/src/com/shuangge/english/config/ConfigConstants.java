package com.shuangge.english.config;

public class ConfigConstants {

	/********************************************************************************************************************************************************/
	
	public static String SERVER_URL = "";
    public static String RES_URL = "";
    public static String RES_URL2 = "";
    public static String IMG_URL = "";
    public static String SND_URL = "";
    public static String RECOMMEND_APP_URL = "";
    
    public static String RES_READ_URL = "";
    
    public static String CONFIG_URL = "";
	public static String FORCED_UPDATE_NUM;
	
	public static String APP_URL = "";
	
	public static int GIFT_VERSION = 1;
	public static String GIFT_CONTENT = "";
	public static int NOTICE_VERSION = 1;
	public static int REWARDS_VERSION = 1;
	
	public static String RES_FAQ_URL = "";
	public static String RES_NOTICE_URL = "";
	public static String RES_REWARDS_HELP_URL = "";
	public static String RES_REWARDS_ClASS_MANAGER_TIP_URL = "";
	public static String RES_REWARDS_CLASS_MEMBER_TIP_URL = "";
	public static String RES_WITHDRAWALS_TIP_URL = "";
    
    public final static boolean DEBUG = true;
    public final static boolean DEBUG_NO_SERVER = false;
    
    public static int SECRET_MSG_MONTH = 3;
    
    public static String getUrl(String url) {
    	return SERVER_URL + url;
    }
    
    /********************************************************************************************************************************************************/
    
    //version
    public static String VERSION_TIP = "";
    public static String FORCED_UPDATE_TIP = "";
    
    //share
    public static String SHARE_IMG = "http://www.happyge.com/icon.png";
    public static String SHARE_TITLE = "赶快加入爽哥和小伙伴共同修炼英语吧";
    public static String SHARE_CONTENT = "爽哥英语：情景化学习神器，快速提升英语听说读能力，从此学习不孤单。";
    public static String SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=air.com.shuangge.phone.ShuangEnglish";
    
    public static String SHARE_IMG2 = "http://www.happyge.com/icon.png";
    public static String SHARE_TITLE2 = "赶快加入爽哥和小伙伴共同修炼英语吧";
    public static String SHARE_CONTENT2 = "爽哥英语：情景化学习神器，快速提升英语听说读能力，从此学习不孤单。";
    public static String SHARE_URL2 = "http://112.124.9.127:9000/shuangge/share/result/";
    
    public static String SHARE_IMG3 = "http://www.happyge.com/icon.png";
    public static String SHARE_TITLE3 = "赶快加入爽哥和小伙伴共同修炼英语吧";
    public static String SHARE_CONTENT3 = "爽哥英语：情景化学习神器，快速提升英语听说读能力，从此学习不孤单。";
    public static String SHARE_URL3 = "http://112.124.9.127:9000/shuangge/share/result/";
    
    public static String SHARE_IMG4 = "http://www.happyge.com/icon.png";
    public static String SHARE_TITLE4 = "赶快加入爽哥和小伙伴共同修炼英语吧";
    public static String SHARE_CONTENT4 = "爽哥英语：情景化学习神器，快速提升英语听说读能力，从此学习不孤单。";
    public static String SHARE_URL4 = "http://112.124.9.127:9000/shuangge/share/result/";
    public static String SHARE_URL5 = "http://112.124.9.127:9000/shuangge/share/result/";
    
    public static String SHARE_TITLE6 = "赶快加入爽哥和小伙伴共同修炼英语吧";
    public static String SHARE_CONTENT6 = "爽哥英语：情景化学习神器，快速提升英语听说读能力，从此学习不孤单。";
    public static String SHARE_URL6 = "http://112.124.9.127:9000/shuangge/share/invitation/";
    
    public static String SHARE_BTN = "";
    
    public static String QQGROUP_KEY = "CrxPGAEbLBoX3U3GCE-mk-CGTwtgJkHH";
    public static String QQGROUP_URL = "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D";
    public static String QQGROUP_NUMBER = "311675643";
    public static String QQAPP_ID = "1103428370";
    public static String QQAPP_KEY = "I8bHbNY1EfcyTdA5";
    
    /********************************************************************************************************************************************************/
    
	
    public final static String ACCOUNT_CHECK_PHONE_TOEKN_FORGET_PWD_URL = "/rest/register/checkForegtPwdPhoneToken";
	public final static String ACCOUNT_FORGET_PWD_BY_PHONE_URL = "/rest/register/foregtPwdByPhone";
	public final static String ACCOUNT_SET_PWD_BY_PHONE_URL = "/rest/register/setPwdByPhone";
	
	public final static String LOGIN_URL = "/api/v1.0/login";//"/login";
	public final static String LOGIN_OAUTH_URL = "/api/v1.0/login/oauth";//"/login/oauth";
	public final static String LOGIN_VISITOR_URL = "/api/v1.0/visitor";///visitor";
	public final static String REGISTER_URL = "/api/v1.0/register";//"/register";//register?loginName=jf1&password=123456&name=jf1&clientType=ios
	public final static String CHECK_LOGIN_NAME_URL = "/rest/register/checkLoginName";
	public final static String CHECK_LOGIN_NAME = "/rest/register/check";
	public final static String GET_REGISTER_PHONE_TOKEN_URL = "/rest/register/getPhoneToken";//register/getPhoneToken?phone=15814089376
	public final static String BINDING_PHONE_URL = "/rest/register/bindingPhone";///register/bindingPhone?phone=15814089376&phoneToken=664029
	public final static String SETTINGS = "/rest/settings";
	public final static String LESSON_TIPS = "/rest/settings/lessonTips";
	public final static String Read_46TIPS = "/rest/settings/46Tips";
	public final static String UNLOCK_TIPS = "/rest/settings/keyTips";
	
	public final static String GET_LESSON_PROGRESS_URL = "/api/v1.0/account/getLessonProgress";//v1.0/account/getLessonProgress?loginName=jf1&token=d863f8f8e98469d979ed0d84dc82d08cced253e7&clientType=ios
	public final static String CONTACT_US_URL = "/rest/feedback";
	
	public final static String UPDATE_INFO_URL = "/api/v1.0/account/updateInfo";
	public final static String ACCOUNT_GETPHONE_TOKEN_URL = "/api/v1.0/account/getPhoneToken";
	public final static String ACCOUNT_BINDING_PHONE_URL = "/api/v1.0/account/bindingPhone";
	public final static String ACCOUNT_SET_PASSWORD_URL = "/api/v1.0/account/setPassword";
	public final static String ACCOUNT_SET_ACCOUNT_URL = "/api/v1.0/account/setAccount";
	public final static String SET_LEVEL_URL = "/api/v1.0/account/setLevel";
	
	public final static String MY_RANK_LIST_URL = "/api/v1.0/account/myRanklist";//
	public final static String ACCOUNT_MYINFO = "/api/v1.0/account/myInfo";
	
	public final static String NOTICE_URL = "/rest/notice";
	
	public final static String ATTENTION_BROWSE_URL = "/rest/attetion/browse";
	public final static String ATTENTION_BROWSE_BY_NAME_URL = "/rest/attetion/browseByName";
	public final static String ATTENTION_URL = "/rest/attetion";
	public final static String ATTENTION_CANCEL_URL = "/rest/attetion/cancel";
	public final static String ATTENTION_RANDOM_URL = "/rest/attetion/random";
	
	
	public final static String LESSON_DETAILS_URL = "/api/v1.0/lesson/getLessonDetails";//loginName=jf1&token=7f491ed59fdcd873264dd6964e4b73abc70f5b54&clientId=11210001
	
	public final static String LESSON_PASS_URL = "/api/v1.0/lesson/pass";//account/getLessonProgress?loginName=jf1&token=d863f8f8e98469d979ed0d84dc82d08cced253e7&clientType=ios
	public final static String LESSON_RESET_URL = "/api/v1.0/lesson/reset";//account/getLessonProgress?loginName=jf1&token=d863f8f8e98469d979ed0d84dc82d08cced253e7&clientType=ios
	public final static String LESSON_UNLOCK = "/rest/lesson/unlock";
	
	public final static String RANK_LIST_URL = "/rest/ranklist";//
	public final static String RANK_LIST_WEEK_URL = "/rest/ranklist/week";//
	public final static String RANK_LIST_ATTENTION_URL = "/rest/ranklist/attention";//
	public final static String RANK_LIST_ATTENTION_WEEK_URL = "/rest/ranklist/attention/week";//
	public final static String RANK_LIST_CLASS_WEEK_URL = "/rest/ranklist/class/week";//
	public final static String RANK_LIST_SCHOOL_WEEK_URL = "/rest/ranklist/school/week";//
	public final static String RANK_LIST_CLASS_LAST_WEEK_URL = "/rest/ranklist/class/last/week";//
	public final static String RANK_LIST_SCHOOL_LAST_WEEK_URL = "/rest/ranklist/school/last/week";//
	public final static String RANK_LIST_USER_LAST_WEEK_URL =	"/ranklist/last/week";
	public final static String RANK_LIST_ATTENTION_LAST_WEEK_URL = "/rest/ranklist/attention/last/week";
	public final static String RANK_LIST_CLASS_ALL_URL = "/rest/ranklist/class/all";
	
	public final static String CLASS_MINE_URL = "/rest/class/getMyInfo";//class/create
	public final static String CLASS_CREATE_URL = "/rest/class/create";//class/create?name=class1&location=city1&description=test&managerId=40&managerName=jf1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_GET_INFO_URL = "/rest/class/getInfo";//class/create?name=class1&location=city1&description=test&managerId=40&managerName=jf1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_LEAVE_URL = "/rest/class/leave";//class/leave?classNo=1&password=123456&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_SEARCH_URL = "/rest/class/search";//class/search?name=class1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_APPLY_GROUP_URL = "/rest/class/apply/group";///class/apply/group?classNo=1&message=hello&userName=jf2&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_APPLY_USER_URL = "/rest/class/apply/user";//apply/user?classNo=1&message=hello&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_REPLY_URL = "/rest/class/reply";//class/reply?msgNo=1&message=hello&result=2&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_UPDATE_INFO_URL = "/rest/class/update";//
	public final static String CLASS_GET_MEMBERS_URL = "/rest/class/getMembers";//class/getMembers?classNo=1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_ASSIGN_AUTHORITY_URL = "/rest/class/assigningAuthority";//class/assigningAuthority?classNo=1&assigningAccount=jf1&authorityLevel=0&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_KICK_URL = "/rest/class/kick";//class/kick?classNo=1&password=123456&kickAccount=jf2&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_INVITE_RANDOM_URL = "/rest/class/invite/random";//class/kick?classNo=1&password=123456&kickAccount=jf2&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String CLASS_RANDOM_URL = "/rest/class/random";//class/kick?classNo=1&password=123456&kickAccount=jf2&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	
	
	public final static String USER_SEARCH_URL = "/rest/user/search";///
	public final static String USER_BROWSER_URL = "/rest/user/browse";///
	
	
	public final static String MSG_GROUP_URL = "/rest/msg/group";///msg/group?pageNo=1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String MSG_SYSTEM_URL = "/rest/msg/system";///msg/group?pageNo=1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	
	public final static String POST_CREATE_URL = "/rest/post/create";//
	public final static String POST_BROWSE_URL = "/rest/post/browse";//
	public final static String POST_CLASS_LIST_URL = "/rest/post/class";//post/class?classNo=1&pageNo=1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	public final static String POST_MY_LIST_URL = "/rest/post/mine";//post/class?classNo=1&pageNo=1&loginName=jf1&token=2c629d5c817ca1a464742288d891a94d3fb1a450
	
	public final static String POST_REPLY_LIST_URL = "/rest/post/reply/list";
	public final static String POST_REPLY_URL = "/rest/post/reply";
	public final static String POST_DELETE_URL = "/rest/post/delete";
	public final static String POST_REPLY_DELETE_URL = "/rest/post/reply/delete";
	public final static String POST_SUB_REPLY_URL = "/rest/post/subReply";
	public final static String POST_ADD_NICE_URL = "/rest/post/addNice";
	public final static String POST_REMOVE_NICE_URL = "/rest/post/removeNice";
	
	
	public final static String PRODUCT_CARE_URL = "/rest/product/care";//参数无  爽店 你关心的 编号列表  你发给我什么返回给你什么
	public final static String PRODUCT_RECORD_URL = "/rest/product/record";//参数productNo（你自定的商品编号） 你关心的商品
	
	public final static String SET_WECHAT_URL = "/api/v1.0/account/setWeChat";//（绑定微信账号）参数 weChatAccount（String 微信账号） password
	public final static String SET_ALIPAY_URL = "/api/v1.0/account/setAlipay";//（绑定支付宝账号）参数 alipayAccount（String 支付宝账号） password
	
	
	
	public final static String PUSH_REGISTER = "/rest/push/register";
	public final static String PUSH_UNREGISTER = "/rest/push/unregister";
	
	public final static String SHARE_RESULT = "/rest/share/result";
	
	public final static String CHANGE_PASSWORD = "/api/v1.0/account/resetPassword";
	public final static String REWARDS_LASTWEEK_RANK = "/rest/rewards/lastweek/list";
	public final static String REWARDS_GET = "/rest/rewards/receive/user";

	
	public final static String READ_URL = "/rest/read";
	public final static String READ_LIST_URL = "/rest/read/list";
	public final static String READ_ADD_WORD_URL = "/rest/read/addWord";
	public final static String READ_REMOVE_WORD_URL = "/rest/read/removeWord";
	public final static String READ_OVERVIEW_URL = "/rest/read/init";
	public final static String READ_WORD_LIST_URL = "/rest/read/newWords";
	public final static String READ_PASSED_WORD_LIST_URL = "/rest/read/newWordsPass"; 
	public final static String READ_PASSED_URL = "/rest/read/pass";  //接口阅读理解
	public final static String READ_PROGRESS_URL = "/rest/read/progress";  //接口文章进度
	
	public static final String SECRET_CREATE_MSG = "/rest/secret/msg/create/msg";
	public static final String SECRET_CREATE_PIC = "/rest/secret/msg/create/pic";
	public static final String SECRET_DELETE_ALL = "/rest/secret/msg/delete";
	public static final String SECRET_DELETE_DETAIL = "/rest/secret/msg/deleteDetail";
	public static final String BLACKLIST_REMOVE = "/rest/blacklist/remove";
	public static final String BLACKLIST_CREATE = "/rest/blacklist/create";
	public static final String SECRET_DETAIL_LIST = "/rest/secret/msg/list";
	public static final String SECRET_MY_ATTENTIONS = "/rest/attetion/attentions";
	public static final String SECRET_MY_FANS = "/rest/attetion/fanses";
//	public static final String SECRET_MY_MSG_LIST = "/rest/secret/msg/mine";
	public static final String SECRET_GET_MEMBERS = "/rest/secret/msg/getMembers";
	public static final String SECRET_SET_STATUS = "/rest/secret/msg/update/status";
//	public static final String SECRET_DETAIL_LIST_UNREAD = "/rest/secret/msg/detail/unRead";
//	public static final String SECRET_MY_MSG_LIST_UNREAD = "/rest/secret/msg/unRead/list";
	
    public final static String REWARDS_UPDATE = "/rest/rewards/convert";
	public final static String REWARDS_SHARE = "/rest/rewards/receive/myself";
	public final static String REWARDS_CODE = "/rest/rewards/code";
	
	public final static String SHOP_GOODS_LIST = "/rest/goods/list";
	public final static String SHOP_ORDER_CREATE = "/rest/order/create";
	public final static String SHOP_ORDER_LIST = "/rest/order/list";
	public final static String SHOP_ORDER_BUY_WX_ORDER_DATA = "/rest/order/wx-order-data ";
	public final static String SHOP_ORDER_BUY_ZFB = "/rest/order/ali-order-query";
	public final static String SHOP_ORDER_BUY_WX = "/rest/order/wx-order-query";
	public final static String SHOP_ORDER_BUY_SCORE = "/rest/order/pay-score";
	public final static String SHOP_ORDER_BUY_CASH = "/api/v1.0/order/pay-deposit";
	public final static String SHOP_ADDRESS_LIST = "/rest/address/list";
	public final static String SHOP_ADDRESS_NEW = "/rest/address/save";
	public final static String SHOP_ADDRESS_DELETE = "/rest/address/delete";
	public final static String SHOP_GOODS_DETAIL = "/rest/goods/detail";
	public final static String SHOP_ORDER_DETAILS = "/rest/order/detail";
	public final static String SHOP_ADDRESS_SETDEFAULT = "/rest/address/setdefault";
	
	public final static String SHARE_LESSON_SUCCESS = "/rest/share/lesson/success";
	
	public final static String LESSON_OBTAIN = "/rest/lesson/obtain";
	public final static String GOODS_LESSON = "/rest/goods/lesson";
	public final static String GIVE_GIFT = "/rest/gift/create";
	public final static String RECEIVE_GIFT = "/rest/gift/receive";
	public final static String USER_SEARCH = "/rest/attetion/search";
	
			
	public final static String WX_APP_ID = "wxab78122b71fe5483"; //微信支付的APP_ID   //wxab78122b71fe5483 wxab78122b71fe5483
	public static final String WX_API_KEY="eb28dd2c943e8b5953c29c55363a3ece";   //微信的API密匙,//  API密钥，在商户平台设置
	public static final String WX_PAY_KEY="gawneljt932jlmvsm293052jfovm2i35";   //微信的API密匙,//  API密钥，在商户平台设置
	public final static String WX_PAY_DATA = "/rest/order/wx-order-data"; //从 [公司服务器] 上获取 [微信支付数据]
	public final static String WX_MCH_ID = "1251319701"; //从 [公司服务器] 上获取 [微信支付数据]

	//支付宝支付的详细信息
	//商户PID
	public static final String PARTNER = "2088611519872488";
	//商户收款账号
	public static final String SELLER = "shuanggekeji@126.com";
	public final static String ALI_NOTIFY_URL = "/order/alipay-notify-url"; // [支付宝服务器] 回调  [公司服务器]
	//支付宝商户私钥
	public final static String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALa2VqJqy7/wtHbehfKwU9cMAt0S+dDe3VNcJJ1tJOcs1PA4Q5rGna2nnTc69nIUNm6gKWtb9tmLA9YxpRgyt8rcuFhYywAosX1sxnWHdRKx9wRgOdvNk5AnNMIaMy7DWAduH0IzJvKhyv1EGfmWfbDhNyRlAZdiozNYgVFBmSVDAgMBAAECgYAHl23kE7HhiLvG0JoaKk9heQNJcjdlAU2K4CI5VEabQFacoInWjXgRtgwnNlD1DnfwgsEVz91izo7bQHbOmZTfS9Nx24Br2CB1abf8J/fRryxVZsmqZQis1fmlbxCm5coWLUnL/qD7faSME71tziEn0Nj+dKgSIc2eIUD3o2UG8QJBAPJ5CwjuSLIcMRgETaARL12mws4crFruF8jDSCWsDPGvUx+Y3fnDUt9mrA0Im777vWMKXX/RwR/t8hZjBujQJmsCQQDA584FqLHFVqJWUboZmJGJtjUONMQEpLr2s1d6rX5Jhz0P0fO+U7l13GY/SyJHSz17R0KmhilheV/TLDbyJ0KJAkEA1lUO3w8a7W4kK3GqWGK4dtUw/9ayuBIcrheIz9wc+QqctKKBHQV+XQG59i901MZcK47/BTyZtSq1Qvq4IdXVDwJBAKqC+3fDEkfVeS8FlJMVaeepOCJzf6R/G4f/JF8axdsmgFHgiiv9A5zrkTF3LziHiDPU3FQnmKJBT/NwTK0lCMkCQQDi4uPc3942xYb7Nx7ZXf2AxyV3Oi6kUt+rtKx5IaMbaLb2k3rGd+5YAadxZc+RXPu5IkzfkeB/5kQyI9duqUN/"; 
	//支付宝公钥
	public final static String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2tlaiasu/8LR23oXysFPXDALdEvnQ3t1TXCSdbSTnLNTwOEOaxp2tp503OvZyFDZuoClrW/bZiwPWMaUYMrfK3LhYWMsAKLF9bMZ1h3USsfcEYDnbzZOQJzTCGjMuw1gHbh9CMybyocr9RBn5ln2w4TckZQGXYqMzWIFRQZklQwIDAQAB"; 
	
	/**
	 * 班级权限
	 * @author jordan
	 *
	 */
	public enum RewardsCode {
		
		invitationCode("邀请码");
		
		RewardsCode(String type) {
			this.type = type;
		}
		
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
	}
	
	public static enum UnlockType {
		
		auto("自动解锁"), autoAndKey("自动加钥匙"), key("仅钥匙");
		
		String type;
		
		UnlockType(String type) {
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
	}
	
	public static enum GoodsType {
		
		real("实物"), virtual("虚拟"), activationCode("激活码");
		
		GoodsType(String type) {
			this.type = type;
		}
		
		private String type;
		
		@Override
		public String toString() {
			return type;
		}
		
	}
	
	public static enum PayType {
		score("积分"), deposit("奖学金"), cash("现金包括微信和支付宝等");
		
		PayType(String type) {
			this.type = type;
		}
		
		String type;
		
		@Override
		public String toString() {
			return this.type;
		}
	}
	
	public static enum GoodsState {
		noSale("下架"), sale("售卖中");
		
		GoodsState(String type) {
			this.type = type;
		}
		
		String type;
		
		@Override
		public String toString() {
			return this.type;
		}
	}


}
