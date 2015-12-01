package com.shuangge.english.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.util.SparseArray;

import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.EntityResType5;
import com.shuangge.english.entity.server.group.ClassMemberResult;
import com.shuangge.english.entity.server.group.ClassResult;
import com.shuangge.english.entity.server.group.LastWeekDedicateResult;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.entity.server.group.RandomResult;
import com.shuangge.english.entity.server.group.SearchClassResult;
import com.shuangge.english.entity.server.lesson.LessonData;
import com.shuangge.english.entity.server.lesson.LessonDetailsResult;
import com.shuangge.english.entity.server.lesson.PassLessonResult;
import com.shuangge.english.entity.server.login.LoginResult;
import com.shuangge.english.entity.server.msg.ModuleMsgData;
import com.shuangge.english.entity.server.msg.NoticeResult;
import com.shuangge.english.entity.server.msg.UserClassGroupResult;
import com.shuangge.english.entity.server.post.PostListResult;
import com.shuangge.english.entity.server.post.PostResult;
import com.shuangge.english.entity.server.post.ReplyListResult;
import com.shuangge.english.entity.server.ranklist.ClassRanklistResult;
import com.shuangge.english.entity.server.ranklist.RanklistResult;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadInitResult;
import com.shuangge.english.entity.server.read.ReadListResult;
import com.shuangge.english.entity.server.read.ReadNewWordsPassResult;
import com.shuangge.english.entity.server.read.ReadNewWordsResult;
import com.shuangge.english.entity.server.read.ReadPassResult;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.secretmsg.AttentionInfoResult;
import com.shuangge.english.entity.server.secretmsg.SecretDetailsResult;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.entity.server.secretmsg.SecretMsgResult;
import com.shuangge.english.entity.server.share.ShareResult;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.shop.AddressListResult;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.GoodsListResult;
import com.shuangge.english.entity.server.shop.GoodsResult;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.entity.server.shop.OrderListResult;
import com.shuangge.english.entity.server.shop.OrderResult;
import com.shuangge.english.entity.server.shop.PayListResult;
import com.shuangge.english.entity.server.shop.WXOrderResult;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.entity.server.user.MyRanklistResult;
import com.shuangge.english.entity.server.user.OtherInfoResult;
import com.shuangge.english.entity.server.user.SearchUserResult;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.about.model.AppDataModel;
//import com.shuangge.english.view.date.model.FriendDataModel;
import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.shuangge.english.view.date.model.FriendDataModel;

/**
 * 数据缓存
 * 
 * @author Jeffrey
 *
 */
public class CacheBeans {

	private String loginName;
	private String token;
	private String deviceToken;
	
	private String defaultLessonId;

	private ModuleMsgData msgDatas = new ModuleMsgData();
	
	/**************************************************************************************************************************************************/
	// 登陆
	private LoginResult loginData;

	// 他人信息
	private OtherInfoResult otherInfoData;

	// 用户推荐
	private SearchUserResult recommendUsers;

	// 用户搜索
	private SearchUserResult searchUsers;

	//当前选择的用户编号
	private Long currentUserNo;
	
	/********************************************************************************************************************************/

	// 我的班组信息
	private MyGroupListResult myGroupDatas;
	
	// 获取班级详情信息
	private ClassResult classData;

	// 班级推荐
	private SearchClassResult recommendClasses;

	// 班级搜索
	private SearchClassResult searchClasses;

	// 班级成员推荐
	private RandomResult recommendMemberData;

	//班级成员
	private ClassMemberResult memberData;
	
	//我当前的班级编号
	private Long currentMyClassNo;
	
	//当前选择的班级编号
	private Long currentClassNo;

	/********************************************************************************************************************************/
	
	// 帖子列表
	private PostListResult postDatas;

	private PostResult postData;

	//当前选择的帖子编号
	private Long currentPostNo;
	
	//当前选择的回复编号
	private Long currentReplyNo;

	private ReplyListResult replyDatas;

	/**************************************************************************************************************************************************/
	
	// 排行榜
	private RanklistResult userRanklistData;
	private RanklistResult userWeekRanklistData;
	private RanklistResult attentionRanklistData;
	private RanklistResult attentionWeekRanklistData;
	private ClassRanklistResult classWeekRanklistData;
	private ClassRanklistResult classLastWeekRanklistData;
	private RanklistResult userLastWeekRanklistData;
	private RanklistResult attentionLastWeekRanklistData;
	private ClassRanklistResult classRanklistData;
	
	//私信
//	private SecretListResult secretListData;
	private SecretMsgResult secretMsgData;
	private SecretDetailsResult secretDetailsResult;
	private Long currentSecretMsgNo;
	private SecretDetailsResult unreadSecretDetailsResult;
	private SecretDetailsResult replySecretDetailsResult;
	private AttentionInfoResult attentionInfoResult;
	private Long currentFriendNo;
	private String currentFriendName;
	private String currentFriendHeadUrl;
	
	private ConcurrentHashMap<Long, SecretMsgDetailData> secretUniqueDatas;
	private List<SecretMsgDetailData> secretListDatas;
	private List<SecretMsgDetailData> secretDetailsDatas = new ArrayList<SecretMsgDetailData>();
	private List<AttentionData> attentionDatas = new ArrayList<AttentionData>();
	private SparseArray<AttentionData> attentionUniqueDatas;
	//上周班级贡献数据
	private LastWeekDedicateResult classMemberRewardRankData;


	/**************************************************************************************************************************************************/
	
	// msg
	private UserClassGroupResult groupMsgData;
	private NoticeResult systemMsgData;

	/**************************************************************************************************************************************************/
	
	//解锁内容
	private LessonData unlockDatas;
	
	//每做完一节的数据返回 包括 结算 解锁等等
	private PassLessonResult passLessonDatas;  
	
	private LessonDetailsResult lessonDetails;
	
	//当前选择的 课程id
	private String currentType1Id;
	private String currentType2Id;
	private String currentType3Id;
	private String currentType4Id;
	private String currentType5Id;
	private String currentType6Id;
	
	private String lastType1Id;
	private String lastType2Id;
	private String lastType2Name;
	private String lastType3Id;
	private String lastType4Id;
	private String lastType4Name;
	private String lastType5Id;
	private String lastType5Name;
	
	//当前m等级下 所有 课程
	private List<EntityResType4> currentType4Datas;
		
	//当前m等级下 所有 课程
	private EntityResType4 currentType4Data;
	
	//当前m等级下 所有 课程
	private EntityResType5 currentType5Data;
	
	//课程结果分享
	private ShareResult shareResult;
	
	/**************************************************************************************************************************************************/

	//设置
	private boolean wifiAutoDownLoad = true;
	private boolean cellularTipsDownLoad = true;
	
	//语音题
	private boolean speechEnabled;
	
	//语音精准度
	private int speechAccuracy = 60;
	
	/**************************************************************************************************************************************************/
	
	private MyRanklistResult myRanklistData;
	
	/**************************************************************************************************************************************************/
	
	//无障碍阅读书籍
	private ReadResult readData;
	private ReadListResult readListData;
	private ReadInitResult readInitResult;
	private ReadNewWordsResult readNewWordsResult;
	private ReadNewWordsPassResult readNewWordsPassResult;
	private ReadPassResult readPassResult;
	
	private Set<IWord> notPassWordsForRead = new HashSet<IWord>();
	private Set<IWord> notPassWordsForLesson = new HashSet<IWord>();
	private Set<IWord> notPassWordsForLLK = new HashSet<IWord>();
	
	public void resetReadCache () {
		this.readData = null;
		this.readListData = null;
		this.readInitResult = null;
		this.readNewWordsResult = null;
		this.readNewWordsPassResult = null;
		this.readPassResult = null;
		this.notPassWordsForRead = new HashSet<IWord>();
	}
	
	//商店
	private AddressListResult addressResult;
	private GoodsListResult goodsListResult;
	private GoodsResult goodsResult;
	private OrderListResult orderListResult;
	private WXOrderResult orderResult;
	private OrderResult cashOrderResult;
	private List<GoodsData> goodsDatasCash = new ArrayList<GoodsData>();
	private List<GoodsData> goodsDatasCredits = new ArrayList<GoodsData>();
	private List<OrderData> orderDatas = new ArrayList<OrderData>();
	private AddressData currentAddress;
	private AddressData defaultAddress;
	private OrderData currentOrderData;
	private String currentOrderId;
	private List<String> orderNos = new ArrayList<String>();
	private List<Integer> errCodes = new ArrayList<Integer>();
	private PayListResult payResult;
	private LessonData obtainLesson;
	private int payType;
	private AttentionData giveUserInfoData;
	private List<AttentionData> searchUserDatas = new ArrayList<AttentionData>();
	private boolean isGive;
	private Integer currentFrequency;
	private List<AppDataModel> appList;
	private boolean isDown = false;
	//约吗
//	private List<FriendDataModel> friendDataList;
	
	private BaseResp wxResp;
	/**************************************************************************************************************************************************/

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LoginResult getLoginData() {
		return loginData;
	}

	public void setLoginData(LoginResult loginData) {
		if (null != loginData) {
			DateUtils.offsetTime = loginData.getServerTime() - System.currentTimeMillis();
			setToken(loginData.getToken());
			setLoginName(loginData.getInfoData().getLoginName());
			if (null != loginData.getSettingsData()) {
				setSpeechAccuracy(loginData.getSettingsData().getSpeechAccuracy());
				setSpeechEnabled(loginData.getSettingsData().getSpeechEnabled());
			}
			if (loginData.getInfoData().getUserType().indexOf(InfoData.ROLE_METEN) != -1) {
				setDefaultLessonId("102"); 
			}
			else {
				setDefaultLessonId("101"); 
			}
			setLastType1Id(loginData.getInfoData().getType1ClientId());
			setLastType2Id(loginData.getInfoData().getType2ClientId());
			setLastType3Id(loginData.getInfoData().getType3ClientId());
			setLastType4Id(loginData.getInfoData().getType4ClientId());
			setLastType5Id(loginData.getInfoData().getType5ClientId());
			setLastType2Name(loginData.getInfoData().getType2Name());
			setLastType4Name(loginData.getInfoData().getType4Name());
			setLastType5Name(loginData.getInfoData().getType5Name());
			if(loginData.getLessonData46() != null) {
				loginData.getLessonData().getType2s().put(loginData.getLessonData46().getClientId(), loginData.getLessonData46());
			}
			setUnlockDatas12(loginData.getLessonData());
		}
		this.loginData = loginData;
	}

	public RanklistResult getUserRanklistData() {
		return userRanklistData;
	}

	public void setUserRanklistData(RanklistResult userRanklistData) {
		this.userRanklistData = userRanklistData;
	}

	public RanklistResult getUserWeekRanklistData() {
		return userWeekRanklistData;
	}

	public void setUserWeekRanklistData(RanklistResult userWeekRanklistData) {
		this.userWeekRanklistData = userWeekRanklistData;
	}

	public RanklistResult getAttentionRanklistData() {
		return attentionRanklistData;
	}

	public void setAttentionRanklistData(RanklistResult attentionRanklistData) {
		this.attentionRanklistData = attentionRanklistData;
	}

	public RanklistResult getAttentionWeekRanklistData() {
		return attentionWeekRanklistData;
	}

	public void setAttentionWeekRanklistData(
			RanklistResult attentionWeekRanklistData) {
		this.attentionWeekRanklistData = attentionWeekRanklistData;
	}

	public ClassRanklistResult getClassLastWeekRanklistData() {
		return classLastWeekRanklistData;
	}

	public void setClassLastWeekRanklistData(
			ClassRanklistResult classLastWeekRanklistData) {
		this.classLastWeekRanklistData = classLastWeekRanklistData;
	}

	public ClassRanklistResult getClassWeekRanklistData() {
		return classWeekRanklistData;
	}

	public void setClassWeekRanklistData(
			ClassRanklistResult classWeekRanklistData) {
		this.classWeekRanklistData = classWeekRanklistData;
	}

	public OtherInfoResult getOtherInfoData() {
		return otherInfoData;
	}

	public void setOtherInfoData(OtherInfoResult otherInfoData) {
		this.otherInfoData = otherInfoData;
	}

	public MyGroupListResult getMyGroupDatas() {
		return myGroupDatas;
	}

	public void setMyGroupDatas(MyGroupListResult myGroupDatas) {
		this.myGroupDatas = myGroupDatas;
	}

	public SearchClassResult getRecommendClasses() {
		return recommendClasses;
	}

	public void setRecommendClasses(SearchClassResult recommendClasses) {
		this.recommendClasses = recommendClasses;
	}

	public PostListResult getPostDatas() {
		return postDatas;
	}

	public void setPostDatas(PostListResult postDatas) {
		this.postDatas = postDatas;
	}

	public Long getCurrentMyClassNo() {
		return currentMyClassNo;
	}

	public void setCurrentMyClassNo(Long currentMyClassNo) {
		this.currentMyClassNo = currentMyClassNo;
	}

	public UserClassGroupResult getGroupMsgData() {
		return groupMsgData;
	}

	public void setGroupMsgData(UserClassGroupResult groupMsgData) {
		this.groupMsgData = groupMsgData;
	}

	public Long getCurrentPostNo() {
		return currentPostNo;
	}

	public void setCurrentPostNo(Long currentPostNo) {
		this.currentPostNo = currentPostNo;
	}

	public PostResult getPostData() {
		return postData;
	}

	public void setPostData(PostResult postData) {
		this.postData = postData;
	}

	public ReplyListResult getReplyDatas() {
		return replyDatas;
	}

	public void setReplyDatas(ReplyListResult replyDatas) {
		this.replyDatas = replyDatas;
	}

	public Long getCurrentUserNo() {
		return currentUserNo;
	}

	public void setCurrentUserNo(Long currentUserNo) {
		this.currentUserNo = currentUserNo;
	}

	public RandomResult getRecommendMemberData() {
		return recommendMemberData;
	}

	public void setRecommendMemberData(RandomResult recommendMemberData) {
		this.recommendMemberData = recommendMemberData;
	}

	public SearchUserResult getSearchUsers() {
		return searchUsers;
	}

	public void setSearchUsers(SearchUserResult searchUsers) {
		this.searchUsers = searchUsers;
	}

	public SearchUserResult getRecommendUsers() {
		return recommendUsers;
	}

	public void setRecommendUsers(SearchUserResult recommendUsers) {
		this.recommendUsers = recommendUsers;
	}

	public SearchClassResult getSearchClasses() {
		return searchClasses;
	}

	public void setSearchClasses(SearchClassResult searchClasses) {
		this.searchClasses = searchClasses;
	}

	public ClassMemberResult getMemberData() {
		return memberData;
	}

	public void setMemberData(ClassMemberResult memberData) {
		this.memberData = memberData;
	}

	public ClassResult getClassData() {
		return classData;
	}

	public void setClassData(ClassResult classData) {
		this.classData = classData;
	}

	public Long getCurrentClassNo() {
		return currentClassNo;
	}

	public void setCurrentClassNo(Long currentClassNo) {
		this.currentClassNo = currentClassNo;
	}

	public Long getCurrentReplyNo() {
		return currentReplyNo;
	}

	public void setCurrentReplyNo(Long currentReplyNo) {
		this.currentReplyNo = currentReplyNo;
	}

	public String getDefaultLessonId() {
		return defaultLessonId;
	}

	public void setDefaultLessonId(String defaultLessonId) {
		this.defaultLessonId = defaultLessonId;
	}

	public LessonData getUnlockDatas() {
		return unlockDatas;
	}

	public void setUnlockDatas12(LessonData unlockDatas) {
		this.unlockDatas = unlockDatas;
	}
	
	public void setUnlockDatas345(LessonData unlockDatas) {
		if (null == unlockDatas) {
			return;
		}
		if (null == this.unlockDatas) {
			this.unlockDatas = unlockDatas;
			return;
		}
		this.unlockDatas.setType3s(unlockDatas.getType3s());
		this.unlockDatas.setType4s(unlockDatas.getType4s());
		this.unlockDatas.setType5s(unlockDatas.getType5s());
	}

	public PassLessonResult getPassLessonDatas() {
		return passLessonDatas;
	}

	public void setPassLessonDatas(PassLessonResult passLessonDatas) {
		setUnlockDatas345(passLessonDatas.getLessonData());
		this.passLessonDatas = passLessonDatas;
	}

	public String getCurrentType1Id() {
		return currentType1Id;
	}

	public void setCurrentType1Id(String currentType1Id) {
		this.currentType1Id = currentType1Id;
		setCurrentType2Id(null);
	}

	public String getCurrentType2Id() {
		return currentType2Id;
	}

	public void setCurrentType2Id(String currentType2Id) {
		this.currentType2Id = currentType2Id;
		setCurrentType3Id(null);
	}

	public String getCurrentType3Id() {
		return currentType3Id;
	}

	public void setCurrentType3Id(String currentType3Id) {
		this.currentType3Id = currentType3Id;
		setCurrentType4Id(null);
	}

	public String getCurrentType4Id() {
		return currentType4Id;
	}

	public void setCurrentType4Id(String currentType4Id) {
		this.currentType4Id = currentType4Id;
		setCurrentType5Id(null);
	}

	public String getCurrentType5Id() {
		return currentType5Id;
	}

	public void setCurrentType5Id(String currentType5Id) {
		this.currentType5Id = currentType5Id;
		if (null != currentType4Data) {
			for (EntityResType5 entityResType5 : currentType4Data.getType5s()) {
				if (!entityResType5.getId().equals(currentType5Id)) {
					continue;
				}
				currentType5Data = entityResType5;
				break;
			}
		}
		setCurrentType6Id(null);
	}

	public String getCurrentType6Id() {
		return currentType6Id;
	}

	public void setCurrentType6Id(String currentType6Id) {
		this.currentType6Id = currentType6Id;
	}

	public String getLastType1Id() {
		return lastType1Id;
	}

	public void setLastType1Id(String lastType1Id) {
		this.lastType1Id = lastType1Id;
	}

	public String getLastType2Id() {
		return lastType2Id;
	}

	public void setLastType2Id(String lastType2Id) {
		this.lastType2Id = lastType2Id;
	}

	public String getLastType2Name() {
		return lastType2Name;
	}

	public void setLastType2Name(String lastType2Name) {
		this.lastType2Name = lastType2Name;
	}

	public String getLastType3Id() {
		return lastType3Id;
	}

	public void setLastType3Id(String lastType3Id) {
		this.lastType3Id = lastType3Id;
	}

	public String getLastType4Id() {
		return lastType4Id;
	}

	public void setLastType4Id(String lastType4Id) {
		this.lastType4Id = lastType4Id;
	}

	public String getLastType4Name() {
		return lastType4Name;
	}

	public void setLastType4Name(String lastType4Name) {
		this.lastType4Name = lastType4Name;
	}

	public String getLastType5Id() {
		return lastType5Id;
	}

	public void setLastType5Id(String lastType5Id) {
		this.lastType5Id = lastType5Id;
	}

	public String getLastType5Name() {
		return lastType5Name;
	}

	public void setLastType5Name(String lastType5Name) {
		this.lastType5Name = lastType5Name;
	}

	public NoticeResult getSystemMsgData() {
		return systemMsgData;
	}

	public void setSystemMsgData(NoticeResult systemMsgData) {
		this.systemMsgData = systemMsgData;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getSpeechAccuracy() {
		return speechAccuracy;
	}

	public void setSpeechAccuracy(int speechAccuracy) {
		this.speechAccuracy = speechAccuracy;
	}

	public boolean isSpeechEnabled() {
		return speechEnabled;
	}

	public void setSpeechEnabled(boolean speechEnabled) {
		this.speechEnabled = speechEnabled;
	}

	public ModuleMsgData getMsgDatas() {
		return msgDatas;
	}

	public void setMsgDatas(ModuleMsgData msgDatas) {
		this.msgDatas = msgDatas;
//		this.msgDatas.setAttentionMsg(false);
//		this.msgDatas.setClassMsg(false);
//		this.msgDatas.setPostMsg(false);
//		this.msgDatas.setSystemMsg(false);
//		if (!msgDatas.isAttentionMsg() && !msgDatas.isClassMsg() && !msgDatas.isPostMsg() && !msgDatas.isSystemMsg()) {
//			return;
//		}
//		Long timestamp = new Date().getTime();
//		if (msgDatas.isAttentionMsg()) {
//			this.msgDatas.setAttentionTimestamp(timestamp);
//			this.msgDatas.setAttentionMsg(true);
//		}
//		if (msgDatas.isClassMsg()) {
//			this.msgDatas.setClassTimestamp(timestamp);
//			this.msgDatas.setClassMsg(true);
//		}
//		if (msgDatas.isPostMsg()) {
//			this.msgDatas.setPostTimestamp(timestamp);
//			this.msgDatas.setPostMsg(true);
//		}
//		if (msgDatas.isSystemMsg()) {
//			this.msgDatas.setSystemTimestamp(timestamp);
//			this.msgDatas.setSystemMsg(true);
//		}
	}

	public List<EntityResType4> getCurrentType4Datas() {
		return currentType4Datas;
	}

	public void setCurrentType4Datas(List<EntityResType4> currentType4Datas) {
		this.currentType4Datas = currentType4Datas;
	}

	public EntityResType4 getCurrentType4Data() {
		return currentType4Data;
	}

	public void setCurrentType4Data(EntityResType4 currentType4Data) {
		this.currentType4Data = currentType4Data;
		if (null != currentType5Id) {
			for (EntityResType5 entityResType5 : currentType4Data.getType5s()) {
				if (!entityResType5.getId().equals(currentType5Id)) {
					continue;
				}
				currentType5Data = entityResType5;
				break;
			}
		}
	}

	public EntityResType5 getCurrentType5Data() {
		return currentType5Data;
	}

	public LessonDetailsResult getLessonDetails() {
		return lessonDetails;
	}

	public void setLessonDetails(LessonDetailsResult lessonDetails) {
		this.lessonDetails = lessonDetails;
	}

	public ShareResult getShareResult() {
		return shareResult;
	}

	public void setShareResult(ShareResult shareResult) {
		this.shareResult = shareResult;
	}

	public MyRanklistResult getMyRanklistData() {
		return myRanklistData;
	}

	public void setMyRanklistData(MyRanklistResult myRanklistData) {
		this.myRanklistData = myRanklistData;
	}

	public ReadResult getReadData() {
		return readData;
	}

	public void setReadData(ReadResult readData) {
		this.readData = readData;
	}
	
	public ReadListResult getReadListData() {
		return readListData;
	}

	public void setReadListData(ReadListResult readListData) {
		this.readListData = readListData;
	}

	public RanklistResult getAttentionLastWeekRanklistData() {
		return attentionLastWeekRanklistData;
	}

	public void setAttentionLastWeekRanklistData(
			RanklistResult attentionLastWeekRanklistData) {
		this.attentionLastWeekRanklistData = attentionLastWeekRanklistData;
	}
	public RanklistResult getUserLastWeekRanklistData() {
		return userLastWeekRanklistData;
	}

	public void setUserLastWeekRanklistData(RanklistResult userLastWeekRanklistData) {
		this.userLastWeekRanklistData = userLastWeekRanklistData;
	}
	
	public ClassRanklistResult getClassRanklistData() {
		return classRanklistData;
	}

	public void setClassRanklistData(ClassRanklistResult classRanklistData) {
		this.classRanklistData = classRanklistData;
	}

	public LastWeekDedicateResult getClassMemberRewardRankData() {
		return classMemberRewardRankData;
	}

	public void setClassMemberRewardRankData(
			LastWeekDedicateResult classMemberRewardRankData) {
		this.classMemberRewardRankData = classMemberRewardRankData;
	}


	public SecretMsgResult getSecretMsgData() {
		return secretMsgData;
	}

	public void setSecretMsgData(SecretMsgResult secretMsgData) {
		this.secretMsgData = secretMsgData;
	}

	public SecretDetailsResult getSecretDetailsResult() {
		return secretDetailsResult;
	}

	public void setSecretDetailsResult(SecretDetailsResult secretDetailsResult) {
		this.secretDetailsResult = secretDetailsResult;
	}

	public Long getCurrentSecretMsgNo() {
		return currentSecretMsgNo;
	}

	public void setCurrentSecretMsgNo(Long currentSecretMsgNo) {
		this.currentSecretMsgNo = currentSecretMsgNo;
	}

	public Long getCurrentFriendNo() {
		return currentFriendNo;
	}

	public void setCurrentFriendNo(Long currentFriendNo) {
		this.currentFriendNo = currentFriendNo;
	}
	
	public String getCurrentFriendHeadUrl() {
		return currentFriendHeadUrl;
	}

	public void setCurrentFriendHeadUrl(String currentFriendHeadUrl) {
		this.currentFriendHeadUrl = currentFriendHeadUrl;
	}

	public String getCurrentFriendName() {
		return currentFriendName;
	}

	public void setCurrentFriendName(String currentFriendName) {
		this.currentFriendName = currentFriendName;
	}

	public SecretDetailsResult getUnreadSecretDetailsResult() {
		return unreadSecretDetailsResult;
	}

	public void setUnreadSecretDetailsResult(
			SecretDetailsResult unreadSecretDetailsResult) {
		this.unreadSecretDetailsResult = unreadSecretDetailsResult;
	}

	public SecretDetailsResult getReplySecretDetailsResult() {
		return replySecretDetailsResult;
	}

	public void setReplySecretDetailsResult(
			SecretDetailsResult replySecretDetailsResult) {
		this.replySecretDetailsResult = replySecretDetailsResult;
	}

	/************************************************************************************************************************/
	
	public Set<IWord> getNotPassWordsForRead() {
		return notPassWordsForRead;
	}

	public void setNotPassWordsForRead(Set<IWord> notPassWordsForRead) {
		this.notPassWordsForRead = notPassWordsForRead;
	}

	public Set<IWord> getNotPassWordsForLesson() {
		return notPassWordsForLesson;
	}

	public void setNotPassWordsForLesson(Set<IWord> notPassWordsForLesson) {
		this.notPassWordsForLesson = notPassWordsForLesson;
	}

	public Set<IWord> getNotPassWordsForLLK() {
		return notPassWordsForLLK;
	}

	public void setNotPassWordsForLLK(Set<IWord> notPassWordsForLLK) {
		this.notPassWordsForLLK = notPassWordsForLLK;
	}

	public List<SecretMsgDetailData> getSecretListDatas() {
		return secretListDatas;
	}

	public void setSecretListDatas(List<SecretMsgDetailData> secretListDatas) {
		this.secretListDatas = secretListDatas;
	}

	public List<SecretMsgDetailData> getSecretDetailsDatas() {
		return secretDetailsDatas;
	}

	public void setSecretDetailsDatas(List<SecretMsgDetailData> secretDetailsDatas) {
		this.secretDetailsDatas = secretDetailsDatas;
	}
	
	private Object lockSecretData = new Object();

	public boolean checkSecretUniqueDatas(Long msgNo, SecretMsgDetailData data) {
		boolean val = true;
		synchronized (lockSecretData) {
			SecretMsgDetailData data1 = secretUniqueDatas.get(msgNo);
			if (null == data1) {
				secretUniqueDatas.put(msgNo, data);
				val = false;
			}
		}
		return val;
	}

	public void setSecretUniqueDatas(
			ConcurrentHashMap<Long, SecretMsgDetailData> secretUniqueDatas) {
		this.secretUniqueDatas = secretUniqueDatas;
	}

	public ConcurrentHashMap<Long, SecretMsgDetailData> getSecretUniqueDatas() {
		return secretUniqueDatas;
	}

	public AttentionInfoResult getAttentionInfoResult() {
		return attentionInfoResult;
	}

	public void setAttentionInfoResult(AttentionInfoResult attentionInfoResult) {
		this.attentionInfoResult = attentionInfoResult;
	}

	public SparseArray<AttentionData> getAttentionUniqueDatas() {
		return attentionUniqueDatas;
	}

	public void setAttentionUniqueDatas(
			SparseArray<AttentionData> attentionUniqueDatas) {
		this.attentionUniqueDatas = attentionUniqueDatas;
	}

	public List<AttentionData> getAttentionDatas() {
		return attentionDatas;
	}

	public void setAttentionDatas(List<AttentionData> attentionDatas) {
		this.attentionDatas = attentionDatas;
	}

	public AddressListResult getAddressResult() {
		return addressResult;
	}

	public void setAddressResult(AddressListResult addressResult) {
		this.addressResult = addressResult;
	}

	public GoodsListResult getGoodsListResult() {
		return goodsListResult;
	}

	public void setGoodsListResult(GoodsListResult goodsListResult) {
		this.goodsListResult = goodsListResult;
	}

	public GoodsResult getGoodsResult() {
		return goodsResult;
	}

	public void setGoodsResult(GoodsResult goodsResult) {
		this.goodsResult = goodsResult;
	}

	public OrderListResult getOrderListResult() {
		return orderListResult;
	}

	public void setOrderListResult(OrderListResult orderListResult) {
		this.orderListResult = orderListResult;
	}

	public WXOrderResult getOrderResult() {
		return orderResult;
	}

	public void setOrderResult(WXOrderResult orderResult) {
		this.orderResult = orderResult;
	}

	public List<GoodsData> getGoodsDatasCash() {
		return goodsDatasCash;
	}

	public void setGoodsDatasCash(List<GoodsData> goodsDatasCash) {
		this.goodsDatasCash = goodsDatasCash;
	}

	public List<GoodsData> getGoodsDatasCredits() {
		return goodsDatasCredits;
	}

	public void setGoodsDatasCredits(List<GoodsData> goodsDatasCredits) {
		this.goodsDatasCredits = goodsDatasCredits;
	}

	public AddressData getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(AddressData currentAddress) {
		this.currentAddress = currentAddress;
	}

	public AddressData getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(AddressData defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public List<OrderData> getOrderDatas() {
		return orderDatas;
	}

	public void setOrderDatas(List<OrderData> orderDatas) {
		this.orderDatas = orderDatas;
	}

	public OrderData getCurrentOrderData() {
		return currentOrderData;
	}

	public void setCurrentOrderData(OrderData currentOrderData) {
		this.currentOrderData = currentOrderData;
	}

	public String getCurrentOrderId() {
		return currentOrderId;
	}

	public void setCurrentOrderId(String currentOrderId) {
		this.currentOrderId = currentOrderId;
	}

	public boolean isMetenUser() {
    	String userType = null;
		try {
			userType = getLoginData().getInfoData().getUserType();
		}
		catch (Exception e) {
			return false;
		}
		return userType.indexOf("meten") != -1;
	}

	public List<String> getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(List<String> orderNos) {
		this.orderNos = orderNos;
	}

	public List<Integer> getErrCodes() {
		return errCodes;
	}

	public void setErrCodes(List<Integer> errCodes) {
		this.errCodes = errCodes;
	}

	public PayListResult getPayResult() {
		return payResult;
	}

	public void setPayResult(PayListResult payResult) {
		this.payResult = payResult;
	}

	public LessonData getObtainLesson() {
		return obtainLesson;
	}

	public void setObtainLesson(LessonData obtainLesson) {
		this.obtainLesson = obtainLesson;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public AttentionData getGiveUserInfoData() {
		return giveUserInfoData;
	}

	public void setGiveUserInfoData(AttentionData giveUserInfoData) {
		this.giveUserInfoData = giveUserInfoData;
	}

	public List<AttentionData> getSearchUserDatas() {
		return searchUserDatas;
	}

	public void setSearchUserDatas(List<AttentionData> searchUserDatas) {
		this.searchUserDatas = searchUserDatas;
	}

	public boolean isGive() {
		return isGive;
	}

	public void setGive(boolean isGive) {
		this.isGive = isGive;
	}

	public Integer getCurrentFrequency() {
		return currentFrequency;
	}

	public void setCurrentFrequency(Integer currentFrequency) {
		this.currentFrequency = currentFrequency;
	}

	public ReadNewWordsResult getReadNewWordsResult() {
		return readNewWordsResult;
	}

	public void setReadNewWordsResult(ReadNewWordsResult readNewWordsResult) {
		this.readNewWordsResult = readNewWordsResult;
	}

	public List<AppDataModel> getAppList() {
		return appList;
	}
	public ReadNewWordsPassResult getReadNewWordsPassResult() {
		return readNewWordsPassResult;
	}
	public void setAppList(List<AppDataModel> appList) {
		this.appList = appList;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}


	public void setReadNewWordsPassResult(ReadNewWordsPassResult readNewWordsPassResult) {
		this.readNewWordsPassResult = readNewWordsPassResult;
	}

	public ReadPassResult getReadPassResult() {
		return readPassResult;
	}

	public void setReadPassResult(ReadPassResult readPassResult) {
		this.readPassResult = readPassResult;
	}

	public ReadInitResult getReadInitResult() {
		return readInitResult;
	}

	public void setReadInitResult(ReadInitResult readInitResult) {
		this.readInitResult = readInitResult;
	}

//	public List<FriendDataModel> getFriendDataList() {
//		return friendDataList;
//	}
//
//	public void setFriendDataList(List<FriendDataModel> friendDataList) {
//		this.friendDataList = friendDataList;
//	}

	public BaseResp getWxResp() {
		return wxResp;
	}

	public void setWxResp(BaseResp wxResp) {
		this.wxResp = wxResp;
	}

//	public List<FriendDataModel> getFriendDataList() {
//		return friendDataList;
//	}
//
//	public void setFriendDataList(List<FriendDataModel> friendDataList) {
//		this.friendDataList = friendDataList;
//	}

	public boolean isWifiAutoDownLoad() {
		return wifiAutoDownLoad;
	}

	public void setWifiAutoDownLoad(boolean wifiAutoDownLoad) {
		this.wifiAutoDownLoad = wifiAutoDownLoad;
	}

	public boolean isCellularTipsDownLoad() {
		return cellularTipsDownLoad;
	}

	public void setCellularTipsDownLoad(boolean cellularTipsDownLoad) {
		this.cellularTipsDownLoad = cellularTipsDownLoad;
	}

	public OrderResult getCashOrderResult() {
		return cashOrderResult;
	}

	public void setCashOrderResult(OrderResult cashOrderResult) {
		this.cashOrderResult = cashOrderResult;
	}

}
