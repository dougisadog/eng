package com.shuangge.english.entity.server;

import android.os.Parcel;

import com.shuangge.english.entity.server.msg.ModuleMsgData;
import com.shuangge.english.support.database.entity.BaseEntity;


/**
 * 
 * @author Jeffrey
 *
 */
public class RestResult extends BaseEntity {

	/**
	 * COMMENT
	 */
	public static final int C_SUCCESS = 0;
	public static final int C_NO_AUTHORITY = 100;
	public static final int C_TIME_OUT = 101;
	public static final int C_IP_ERR = 102;
	public static final int C_MAC_NULL = 103;
	public static final int C_UID_NULL = 104;
	public static final int C_PARAMS_ERR = 200;
	public static final int C_TOKEN_ERR = 201;
	public static final int C_SERVER_CONNECT_ERR = 202;
	
	public static final int C_LOCK = 209;
	public static final int C_CONTAINS_SENSITIVE_WORDS = 900;
	
	public static final String M_SUCCESS = "Success";
	public static final String M_NO_AUTHORITY = "对不起,您没有权限执行此操作!";
	public static final String M_TIME_OUT = "服务器连接超时，请检查网络!";
	public static final String M_IP_ERR = "IP地址不正确!";
	public static final String M_MAC_NULL = "MAC地址不能为空!";
	public static final String M_UID_NULL = "UID不能为空!";
	public static final String M_PARAMS_ERR = "参数错误!";
	public static final String M_TOKEN_ERR = "您的账号已在其它客户端登录，请重新登录";
	public static final String M_SERVER_CONNECT_ERR = "服务器连接失败,请重新登陆";
	
	public static final String M_LOCK = "服务器维护中……";
	public static final String M_CONTAINS_SENSITIVE_WORDS = "包含不当内容，发送失败！";
	
	/*************************************************************************************/
	
	
	/**
	 * ACCOUNT
	 */
	public static final int C_ACCOUNT_FORMAT_ERR = 211;
	public static final int C_ACCOUNT_ALREADY_EXISTS = 212;
	public static final int C_ACCOUNT_DOES_NOT_EXIST = 213;
	public static final int C_ACCOUNT_PASSWORD_ERR =214;
	public static final int C_ACCOUNT_CENTER_REQUEST_ERR = 215;
	public static final int C_NAME_REPEAT = 216;
	public static final int C_NAME_PARAMS_NULL = 217;
	public static final int C_NAME_SIZE_ERR = 218;
	public static final int C_LOGINNAME_SIZE_ERR = 219;
	public static final int C_LOGIN_PARAMS_NULL = 220;
	public static final int C_REGISTER_PARAM_NULL = 221;
	public static final int C_PASSWORD_SIZE_ERR = 222;
	public static final int C_PASSWORD_NULL = 223;
	public static final int C_TWO_PASSWORDS_NOT_MATCH = 224;
	public static final int C_PHONE_REPEAT = 225;
	public static final int C_PHONE_TOKEN_ERR = 226;
	public static final int C_PHONE_BINDING_TIME_OUT = 227;
	public static final int C_PHONE_TOEKN_REPEAT_REQUEST = 228;
	public static final int C_PHONE_FORMAT_ERR = 229;
	public static final int C_PHONE_PARAM_NULL = 230;
	public static final int C_USER_DOES_NOT_EXIST = 231;
	public static final int C_VISITOR_CAN_NOT_BIND_PHONE = 232;
	public static final int C_VISITOR_CAN_NOT_BIND_WECHAT = 233;
	public static final int C_VISITOR_CAN_NOT_BIND_ALIPAY = 234;
	public static final int C_VISITOR_CAN_NOT_REST_PASS = 239;
	public static final int C_USER_CAN_NOT_REGISTER_AGAIN = 235;
	public static final int C_USER_CAN_NOT_SET_PASSWORD = 236;
	public static final int C_LOGINNAME_FORMAT_ERR = 237;
	public static final int C_WECHAT_NO_PARAMS_NULL = 238;
	
	public static final String M_ACCOUNT_FORMAT_ERR = "当前账号格式有误,请重新输入";
	public static final String M_ACCOUNT_ALREADY_EXISTS = "当前账号已存在,请重新输入";
	public static final String M_ACCOUNT_DOES_NOT_EXIST = "当前账号不存在,请检查账号是否输入正确";
	public static final String M_ACCOUNT_PASSWORD_ERR = "密码错误,请重新输入";
	public static final String M_ACCOUNT_CENTER_REQUEST_ERR = "数据中心请求错误";
	public static final String M_NAME_REPEAT = "昵称重复,请重新输入";
	public static final String M_NAME_PARAMS_NULL = "昵称不能为空";
	public static final String M_NAME_SIZE_ERR = "昵称长度限制为1-20个字符";
	public static final String M_LOGINNAME_SIZE_ERR = "登录名长度限制为3-20个字符";
	public static final String M_LOGIN_PARAMS_NULL = "账号/密码不能为空";
	public static final String M_REGISTER_PARAM_NULL = "账号/密码/昵称不能为空";
	public static final String M_PASSWORD_SIZE_ERR = "密码长度限制为6-20个字符";
	public static final String M_PASSWORD_NULL = "密码不能为空";
	public static final String M_TWO_PASSWORDS_NOT_MATCH = "两次密码不一致";
	public static final String M_PHONE_REPEAT = "当前手机号已绑定,请重新输入";
	public static final String M_PHONE_TOKEN_ERR = "短信验证码输入错误,请重新输入";
	public static final String M_PHONE_BINDING_TIME_OUT = "短信验证码输入超时,请重新输入";
	public static final String M_PHONE_TOEKN_REPEAT_REQUEST = "短信验证码已发送";
	public static final String M_PHONE_FORMAT_ERR = "当前手机号格式有误,请重新输入";
	public static final String M_PHONE_PARAM_NULL = "手机号不能为空";
	public static final String M_USER_DOES_NOT_EXIST = "当前用户不存在";
	public static final String M_VISITOR_CAN_NOT_BIND_PHONE = "第三方登录用户不能绑定手机,请补全账号信息";
	public static final String M_VISITOR_CAN_NOT_BIND_WECHAT = "第三方登录用户不能绑定微信,请补全账号信息";
	public static final String M_VISITOR_CAN_NOT_BIND_ALIPAY = "第三方登录用户不能绑定支付宝,请补全账号信息";
	public static final String M_VISITOR_CAN_NOT_REST_PASS = "第三方登录用户无权修改密码，请不全账号信息";
	public static final String M_USER_CAN_NOT_REGISTER_AGAIN = "账号已经补全,重复请求";
	public static final String M_USER_CAN_NOT_SET_PASSWORD = "用户密码已经补全,重复请求";
	public static final String M_LOGINNAME_FORMAT_ERR = "账号格式不正确";
	public static final String M_WECHAT_NO_PARAMS_NULL = "微信号不能为空";
	
	/*************************************************************************************/
	
	/**
	 * UPLOAD
	 */
	public static final int C_FILENAME_REPREAT = 240;
	public static final int C_UPLOAD_ERR = 241;
	public static final int C_UPLOAD_LARGER_THAN_1M = 242;
	public static final int C_UPLOAD_SIZE_THAN_SIX = 243;
	public static final int C_UPLOAD_SIZE_THAN_EIGHT = 244;
	
	public static final String M_FILENAME_REPREAT = "上传图片名称重复";
	public static final String M_UPLOAD_ERR = "上传失败";
	public static final String M_UPLOAD_LARGER_THAN_1M = "上传图片大小不能大于1M";
	public static final String M_UPLOAD_SIZE_THAN_SIX = "上传图片数量不能超过6张";
	public static final String M_UPLOAD_SIZE_THAN_EIGHT = "上传图片数量不能超过8张";
	
	/*************************************************************************************/
	
	/**
	 * ATTENTION
	 */
	public static final int C_ATTENTION_ERR = 250;
	public static final int C_ATTENTION_REPEAT = 251;
	public static final int C_ATTENTION_NO = 255;
	
	public static final String M_ATTENTION_ERR = "无法关注自己";
	public static final String M_ATTENTION_REPEAT = "您已关注此人";
	public static final String M_ATTENTION_NO = "您还没关注此人";

	public static final int C_BLACKLIST_NO = 253;
	public static final int C_BLACKLIST_WITHIN = 254;
	
	public static final String M_BLACKLIST_NO = "此人不在您的黑名单内";
	public static final String M_BLACKLIST_WITHIN = "您在对方的黑名单内";
	
	/**************************************************************************************/
	
	/**
	 * TYPE
	 */
	public static final int C_TYPE_DOES_NOT_EXIST = 260;
	
	public static final String M_TYPE_DOES_NOT_EXIST = "Type doesn't exist!";
	
	/**
	 * CLASS
	 */
	public static final int C_CLASS_NAME_REPEAT = 301;
	public static final int C_CLASS_NAME_PARAMS_NULL = 302;
	public static final int C_ALREADY_JOIN_CLASS = 303;
	public static final int C_ALREADY_JOIN_SCHOOL = 304;
	public static final int C_CAN_ONLY_JOIN_ONE_CLASS = 305;
	public static final int C_CLASS_MUST_BE_HANDED_OVER_SB = 310;
	public static final int C_CLASS_HAS_ALREADY_BEEN_FULL  = 311;
	public static final int C_CLASS_DOES_NOT_EXIST  = 312;
	public static final int C_CLASS_MEMBER_NUM_LESS_THAN_20 = 313;
	public static final int C_CLASS_MEMBER_NUM_FULL = 314;
	public static final int C_CLASS_PHONE_NO_BINDING = 315;
	public static final int C_NOT_KICK_YOURSELF = 316;
	public static final int C_NOT_THE_CREATER = 317;
	public static final int C_USER_DOES_NOT_BELONG_TO_CLASS =318;
	public static final int C_CLASS_REPEAT_REQUEST = 319;
	public static final int C_JOINED_THE_OTHER_CLASS = 320;
	public static final int C_YOU_HAVE_TO_JOIN_THE_OTHER_CLASS = 327;
	public static final int C_IS_NOT_THE_CURRENT_LEVEL = 322;
	public static final int C_VISITOR_CAN_NOT_CREATE_CLASS = 323;
	public static final int C_VISITOR_CAN_NOT_REPLY_CLASS = 324;
	public static final int C_CLASS_CAN_ONLY_HAVE_THREE_VICE_MONITOR = 325;
	public static final int C_CLASS_REPLY_REPEAT_REQUEST = 326;
	public static final int C_APPLY_DOES_NOT_EXIST = 327;
	public static final int C_USER_HAS_ALREADY_BEEN_AGREE_OR_REFUSED = 328;
	public static final int C_YOU_ARE_NOT_IN_CLASS = 329;
	public static final int C_STUDENT_WITHOUT_THIS_AUTHORITY = 330;
	
	
	public static final String M_CLASS_NAME_REPEAT = "班级名称重复,请重新输入";
	public static final String M_CLASS_NAME_PARAMS_NULL = "班级名称不能为空";
	public static final String M_ALREADY_JOIN_CLASS = "您已经加入了班级,不能创建班级";
	public static final String M_ALREADY_JOIN_SCHOOL = "您已经加入了学校,不能创建学校";
	public static final String M_CAN_ONLY_JOIN_ONE_CLASS = "每人只能加入一个班级";
	public static final String M_CLASS_MUST_BE_HANDED_OVER_SB = "您必须指定一个班长人选,才能退出班级";
	public static final String M_CLASS_HAS_ALREADY_BEEN_FULL  = "班级人数达到上限,请清除成员后才能加入";
	public static final String M_CLASS_DOES_NOT_EXIST  = "班级不存在";
	public static final String M_CLASS_MEMBER_NUM_LESS_THAN_20 = "班级成员数量少于20人";
	public static final String M_CLASS_MEMBER_NUM_FULL = "您加入的班级人数已超过上限";
	public static final String M_CLASS_PHONE_NO_BINDING = "您还没有绑定手机";
	public static final String M_NOT_KICK_YOURSELF = "不能把自己移出班级";
	public static final String M_NOT_THE_CREATER = "不能把班长移出班级";
	public static final String M_USER_DOES_NOT_BELONG_TO_CLASS ="当前用户已不存在此班级";
	public static final String M_CLASS_REPEAT_REQUEST = "请勿重复申请";
	public static final String M_JOINED_THE_OTHER_CLASS = "此人已加入其它班级";
	public static final String M_YOU_HAVE_TO_JOIN_THE_OTHER_CLASS = "您已经加入其它班级";
	public static final String M_IS_NOT_THE_CURRENT_LEVEL = "任命副班长失败";
	public static final String M_VISITOR_CAN_NOT_CREATE_CLASS = "游客无法创建班级";
	public static final String M_VISITOR_CAN_NOT_REPLY_CLASS = "游客无法申请加入班级";
	public static final String M_CLASS_CAN_ONLY_HAVE_THREE_VICE_MONITOR = "副班长名额最高为3个,您无法继续任命副班长";
	public static final String M_CLASS_REPLY_REPEAT_REQUEST = "请勿重复邀请";
	public static final String M_APPLY_DOES_NOT_EXIST = "申请信息不存在";
	public static final String M_USER_HAS_ALREADY_BEEN_AGREE_OR_REFUSED = "您已经进行此操作";
	public static final String M_YOU_ARE_NOT_IN_CLASS = "您不在此班级";
	public static final String M_STUDENT_WITHOUT_THIS_AUTHORITY = "普通学员无此操作权限";
	
	/***********************************************************************************************/
	
	/**
	 * POST
	 */
	public static final int C_POST_REPEAT_TO_ADD_NICE = 401;
	public static final int C_POST_REPEAT_TO_REMOVE_NICE = 402;
	public static final int C_POST_DOES_NOT_EXIST = 403;
	public static final int C_POST_REPLY_DOES_NOT_EXIST = 404;
	public static final int C_POST_TITLE_PARAM_NULL = 405;
	public static final int C_POST_CONTENT_PARAM_NULL = 406;
	public static final int C_REPLY_CONTENT_PARAM_NULL = 407;
	
	public static final String M_POST_REPEAT_TO_ADD_NICE = "您已经赞过此帖子,请不要重复操作";
	public static final String M_POST_REPEAT_TO_REMOVE_NICE = "您已经取消赞,请不要重复操作";
	public static final String M_POST_DOES_NOT_EXIST = "帖子不存在";
	public static final String M_POST_REPLY_DOES_NOT_EXIST = "帖子回复不存在";
	public static final String M_POST_TITLE_PARAM_NULL= "帖子标题不能为空";
	public static final String M_POST_CONTENT_PARAM_NULL= "帖子内容不能为空";
	public static final String M_REPLY_CONTENT_PARAM_NULL= "回复内容不能为空";
	
	/***********************************************************************************************/
	
	/**
	 * PRODUCT
	 */
	public static final int C_PRODUCT_DOSE_NOT_EXIST = 500;
	
	public static final String M_PRODUCT_DOSE_NOT_EXIST = "产品不存在";
	
	/***********************************************************************************************/
	
	/**
	 * LESSON
	 */
	public static final int C_ACCESS_TO_THE_DETAILS_ERR = 600;
	public static final int C_YOUR_LIFE_HAS_RUN_OUT = 601;
	
	public static final String M_ACCESS_TO_THE_DETAILS_ERR = "获取课程信息失败";
	public static final String M_YOUR_LIFE_HAS_RUN_OUT = "您已经没有生命值了";
	
	
	/***********************************************************************************************/
	
	/**
	 * FEEDBACK
	 */
	public static final int C_FEEDBACK_TITLE_PARAM_NULL = 700;
	public static final int C_FEEDBACK_DESCRIPTION_PARAM_NULL = 701;
	
	public static final String M_FEEDBACK_TITLE_PARAM_NULL = "标题不能为空";
	public static final String M_FEEDBACK_DESCRIPTION_PARAM_NULL = "反馈信息不能为空";
	
	/***********************************************************************************************/
	
	
	/**
	 * TRADE
	 */
	public static final int C_LOCK_OF_MONEY = 800;
	public static final int C_NOT_SUFFICIENT_FUNDS = 801;
	public static final int C_CONVERT_MONEY_NULL_ERR = 802;
	public static final int C_USER_MONEY_NULL_ERR = 803;
	
	public static final String M_LOCK_OF_MONEY = "余额已被锁定";
	public static final String M_NOT_SUFFICIENT_FUNDS = "余额不足";
	public static final String M_CONVERT_MONEY_NULL_ERR = "提取现金金额有误";
	public static final String M_USER_MONEY_NULL_ERR = "您的余额不足，无法进行提现";
	
	/***********************************************************************************************/
	
	public static final int C_SECRET_MSG_CONTENT_PARAM_NULL = 800;
	public static final int C_SECRET_MSG_DOSE_NOT_EXIST = 801;
	public static final int C_SECRET_MSG_ERR = 802;
	
	public static final String M_SECRET_MSG_CONTENT_PARAM_NULL = "私信内容不能为空";
	public static final String M_SECRET_MSG_DOSE_NOT_EXIST = "当前私信不存在";
	public static final String M_SECRET_MSG_ERR = "不能给自己发私信";
	
	/***********************************************************************************************/
	
	/**
	 * Reward
	 */
	
	
	/***********************************************************************************************/
	
	/***********************************************************************************************/
	
	private int code = C_SUCCESS;
	private String msg = M_SUCCESS;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	private ModuleMsgData moduleMsgData;

	public ModuleMsgData getModuleMsgData() {
		return moduleMsgData;
	}

	public void setModuleMsgData(ModuleMsgData moduleMsgData) {
		this.moduleMsgData = moduleMsgData;
	}
	
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	private Boolean weakenMemory = false;

	public Boolean getWeakenMemory() {
		return weakenMemory;
	}

	public void setWeakenMemory(Boolean weakenMemory) {
		this.weakenMemory = weakenMemory;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}

	@Override
	public BaseEntity getEntity() {
			return null;
	}
	
}
