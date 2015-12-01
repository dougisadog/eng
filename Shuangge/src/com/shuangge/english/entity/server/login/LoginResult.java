package com.shuangge.english.entity.server.login;

import java.util.List;

import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.LessonData;
import com.shuangge.english.entity.server.lesson.Type2Data;
import com.shuangge.english.entity.server.msg.ADData;
import com.shuangge.english.entity.server.read.RewardSettingsData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.entity.server.user.RecommendData;
import com.shuangge.english.entity.server.user.RewardsData;
import com.shuangge.english.entity.server.user.SettingsData;


/**
 * @author Jeffrey
 *
 */
public class LoginResult extends RestResult {

	private Boolean first = false;
	private LessonData lessonData;
	
	private InfoData infoData;
	private RewardsData rewardsData;
	private SettingsData settingsData;
	private List<ADData> ads;
	private List<RecommendData> recommends;
	private List<Long> classNos;
	private Long schoolNo;
	private Long serverTime;
	private List<String> clientIds;
	private List<RewardSettingsData> rewardSettingsData;
	private Type2Data lessonData46;

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public LessonData getLessonData() {
		return lessonData;
	}

	public void setLessonData(LessonData lessonData) {
		this.lessonData = lessonData;
	}

	public InfoData getInfoData() {
		return infoData;
	}

	public void setInfoData(InfoData infoData) {
		this.infoData = infoData;
	}

	public SettingsData getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(SettingsData settingsData) {
		this.settingsData = settingsData;
	}

	public List<ADData> getAds() {
		return ads;
	}

	public void setAds(List<ADData> ads) {
		this.ads = ads;
	}

	public List<RecommendData> getRecommends() {
		return recommends;
	}

	public void setRecommends(List<RecommendData> recommends) {
		this.recommends = recommends;
	}

	public List<Long> getClassNos() {
		return classNos;
	}

	public void setClassNos(List<Long> classNos) {
		this.classNos = classNos;
	}

	public Long getSchoolNo() {
		return schoolNo;
	}

	public void setSchoolNo(Long schoolNo) {
		this.schoolNo = schoolNo;
	}

	public RewardsData getRewardsData() {
		return rewardsData;
	}

	public void setRewardsData(RewardsData rewardsData) {
		this.rewardsData = rewardsData;
	}

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	public List<RewardSettingsData> getRewardSettingsData() {
		return rewardSettingsData;
	}

	public void setRewardSettingsData(List<RewardSettingsData> rewardSettingsData) {
		this.rewardSettingsData = rewardSettingsData;
	}

	public List<String> getClientIds() {
		return clientIds;
	}

	public void setClientIds(List<String> clientIds) {
		this.clientIds = clientIds;
	}

	public Type2Data getLessonData46() {
		return lessonData46;
	}

	public void setLessonData46(Type2Data lessonData46) {
		this.lessonData46 = lessonData46;
	}


}
