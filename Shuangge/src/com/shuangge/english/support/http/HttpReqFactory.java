package com.shuangge.english.support.http;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpProcess.DownloadState;
import com.shuangge.english.support.http.HttpReqHelper.UploadFileFileListener;
import com.shuangge.english.support.utils.DESUtils;

/**
 * HTTP 网络请求类
 * 
 * @author Jeffrey
 *
 */
public class HttpReqFactory {

	public static boolean createGetReq(HttpReqHelper.HttpReqListener listener, String url, ReqParam... params) {
		return new HttpProcess().doGet(url, params, listener);
	}

	public static boolean createPostReq(HttpReqHelper.HttpReqListener listener, String url, ReqParam... params) {
		return new HttpProcess().doPost(url, params, listener);
	}

	public static boolean createDownloadFileReq(HttpReqHelper.DownloadFileListener listener, String url, DownloadState state, File file) {
		return !Thread.currentThread().isInterrupted() && new HttpProcess().doGetAndSaveFile(url, file, state, listener);
	}

	public static boolean createDownloadFileReq(HttpReqHelper.DownloadBinaryFileListener listener, String url) {
		return !Thread.currentThread().isInterrupted() && new HttpProcess().doGetFile(url, listener);
	}

	public static boolean createUpdateFileReq(HttpReqHelper.UploadFileFileListener listener, String url, ReqFile[] fileParams, ReqParam... params) {
		return !Thread.currentThread().isInterrupted() && new HttpProcess().doUploadFile(url, fileParams, params, listener);
	}

	/**************************************************************************************************************************************/

	public static <T> T getServerResult(final Class<T> clazz, String url, ReqParam... params) {
		ReqParam param = DESUtils.encryptFromParams(params);
		return new ServerResultProcessor<T>().doPost(clazz, url, new ReqParam[] { param }, null).getResult();
	}

	public static <T> T getServerResultByToken(final Class<T> clazz, String url, ReqParam... params) {
		ReqParam[] reqParams = new ReqParam[params.length + 2];
		for (int i = 0; i < params.length; i++) {
			reqParams[i] = params[i];
		}
		reqParams[params.length] = new ReqParam("loginName", GlobalRes.getInstance().getBeans().getLoginName());
		reqParams[params.length + 1] = new ReqParam("token", GlobalRes.getInstance().getBeans().getToken());
		ReqParam param = DESUtils.encryptFromParams(reqParams);
		return new ServerResultProcessor<T>().doPost(clazz, url, new ReqParam[] { param }, null).getResult();
	}

	public static <T> T getServerResultByToken(final Class<T> clazz, String url, List<ReqFile> files, ReqParam... params) {
		ReqParam[] reqParams = new ReqParam[params.length + 2];
		for (int i = 0; i < params.length; i++) {
			reqParams[i] = params[i];
		}
		reqParams[params.length] = new ReqParam("loginName", GlobalRes.getInstance().getBeans().getLoginName());
		reqParams[params.length + 1] = new ReqParam("token", GlobalRes.getInstance().getBeans().getToken());
		ReqParam param = DESUtils.encryptFromParams(reqParams);
		ReqFile[] files2 = new ReqFile[files.size()];
		return new ServerResultProcessor<T>().doPost(clazz, url, new ReqParam[] { param }, files.toArray(files2)).getResult();
	}

	private static class ServerResultProcessor<T> {

		private T result;

		public ServerResultProcessor<T> doPost(final Class<T> clazz, String url, ReqParam[] params, ReqFile[] files) {
			if (null != files) {
				new HttpProcess().doUploadFile(ConfigConstants.getUrl(url), files,
						params, new UploadFileFileListener() {

							@Override
							public void waitServerResponse() {
							}

							@Override
							public void progressHandler(long progress, long max) {
							}

							@Override
							public void errorHandler(Exception e, String errMsg) {
								serverResultErrorHandler(errMsg);
							}

							@Override
							public void completeHandler(String str) {
								serverResultCompleteHandler(str, clazz);
							}
						});
				return this;
			}
			new HttpProcess().doPost(ConfigConstants.getUrl(url), params,
					new HttpReqHelper.HttpReqListener() {

						@Override
						public void errorHandler(Exception e, String errMsg) {
							serverResultErrorHandler(errMsg);
						}

						@Override
						public void completeHandler(String str) {
							serverResultCompleteHandler(str, clazz);
						}

					});
			return this;

		}

		public T getResult() {
			return result;
		}

		private void serverResultErrorHandler(String errMsg) {
			if (!TextUtils.isEmpty(errMsg)) {
				GlobalApp.getInstance().showCustomErrMsg(errMsg);
			}
			result = null;
		}

		private void serverResultCompleteHandler(String str, Class<T> clazz) {
			try {
				str = DESUtils.decrypt(str);
				// Gson gson = new Gson();
				Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
				result = gson.fromJson(str, clazz);
			}
			catch (Exception e) {
				android.util.Log.e("test", "gson or base64 error: " + str + "___" + e.getMessage());
			}
			RestResult serverInfo = (RestResult) result;
			if (null == serverInfo) {
//				GlobalApp.getInstance().showConnectServerErrMsg();
				return;
			}
			if (serverInfo.getCode() != RestResult.C_SUCCESS) {
				//超时 重新登陆
				if (serverInfo.getCode() == 201) {
					GlobalApp.getInstance().resetToLogin();
				}
				GlobalApp.getInstance().showServerErrMsg(serverInfo);
				return;
			}
			if (null != serverInfo.getModuleMsgData())
				GlobalRes.getInstance().getBeans().setMsgDatas(serverInfo.getModuleMsgData());
			if (!TextUtils.isEmpty(serverInfo.getToken())) {
				GlobalRes.getInstance().getBeans().setToken(serverInfo.getToken());
			}
		}

		public class DateDeserializer implements JsonDeserializer<Date> {

			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
				Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
				Matcher matcher = pattern.matcher(json.getAsJsonPrimitive().getAsString());
				String result = matcher.replaceAll("$2");
				return new Date(Long.valueOf(result));
			}

		}

	}

	/**************************************************************************************************************************************/

	/**
	 * 请求时参数
	 * 
	 * @author Jeffrey
	 *
	 */
	public static class ReqParam {

		public ReqParam(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		private String key;
		private Object value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}

	/**
	 * 请求时File
	 * 
	 * @author Jeffrey
	 *
	 */
	public static class ReqFile {

		public static final String TYPE_IMAGE_PNG = "application/octet-stream";

		public ReqFile(String key, File file, String fileType) {
			this(key, file, fileType, "file");
		}

		public ReqFile(String key, File file, String fileType, String feildName) {
			this.key = key;
			this.file = file;
			this.fileType = fileType;
			this.feildName = feildName;
		}

		private String key;
		private File file;
		private String feildName;
		private String fileType;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getFeildName() {
			return feildName;
		}

		public void setFeildName(String feildName) {
			this.feildName = feildName;
		}

	}

}
