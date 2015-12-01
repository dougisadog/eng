package com.shuangge.english.network.read;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadContentData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRead extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqRead(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		ReadResult rr = beans.getReadData();
		//若内存中为相同文章 则无需请求 只讲问题的答案选项重置
		if (null != rr && rr.getReadNo().longValue() ==  ((Long)params[0]).longValue()) {
			rr.setReset(true);
			return true;
		}
		ReadResult result = HttpReqFactory.getServerResultByToken(ReadResult.class, ConfigConstants.READ_URL,
				new HttpReqFactory.ReqParam("readNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			if (null == result.getProgress()) {
				result.setProgress(ReadResult.PROGRESS_L1);
			}
			
			Set<IWord> coreWords = new HashSet<IWord>();
			ReadWordData word = null;
			for (Long key: result.getWordMap().keySet()) {
				word = result.getWordMap().get(key);
				if (word.isCore4()) {
					coreWords.add(word);
				}
				if (!TextUtils.isEmpty(word.getImgUrl()))
					word.setImgUrl(word.getImgUrl().replace(" ", "%20"));
				else
					word.setImgUrl("http://112.124.9.127:8081/res/read/pic/people1_20150624082004_7.jpg");
				if (!TextUtils.isEmpty(word.getSoundUrl()))
					word.setSoundUrl(word.getSoundUrl().replace(" ", "%20"));
				else
					word.setSoundUrl("http://112.124.9.127:8081/res/read/sound/us_20150624154711_726.mp3");
			}
			result.setReadNo((Long)params[0]);
			
			for (int i = 0; i < result.getContents().size(); i++) {
				ReadContentData readContentData = result.getContents().get(i);
				//每次请求文章数据时将 文章相关缓存清空
				if (null != readContentData && null != readContentData.getContent()) {
					readContentData.setContent("  " + readContentData.getContent().replace("’", "'").replace("”", "\"").replace("“", "\""));
//					readContentData.getContent().replace("’", "'");
//					readContentData.getContent().replace("”", "\"");
//					readContentData.getContent().replace("“", "\"");
					//用于匹配单字母开头的翻译匹配
//					readContentData.setContent("  " + readContentData.getContent());
				}
			}
			Set<IWord> words = new HashSet<IWord>();
			for (Long id : result.getUserWordIds()) {
				if (null != result.getWordMap().get(id)) {
					words.add(result.getWordMap().get(id));
				}
			}
			
			
			result.setCoreWords(coreWords);
			
			beans.setReadData(result);
			beans.setNotPassWordsForRead(words);
			beans.setNotPassWordsForLesson(new HashSet<IWord>());
			beans.setNotPassWordsForLLK(new HashSet<IWord>());
			
			beans.getReadInitResult().setReadNo(((Long)params[0]));
			return true;
		}
		return false;
	}
	
}
