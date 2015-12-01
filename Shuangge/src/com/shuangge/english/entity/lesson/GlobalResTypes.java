package com.shuangge.english.entity.lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoResType2;
import com.shuangge.english.dao.DaoResType4;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.DownloadHelper;
import com.shuangge.english.support.http.HttpProcess.CallbackDownloadState;
import com.shuangge.english.support.http.HttpProcess.DownloadState;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.service.Type4DownLoadService;
import com.shuangge.english.support.task.MyAsyncTask;
import com.shuangge.english.support.utils.ImageUtils;
import com.shuangge.english.support.utils.ServiceUtils;
import com.shuangge.english.support.utils.ServiceUtils.ServiceIntentCallBack;
import com.shuangge.english.support.utils.ZipUtils;
import com.shuangge.english.task.TaskCheckCatalog;

/**
 * 课程资源管理器
 * @author Jeffrey
 *
 */
public class GlobalResTypes {

	public static Map<String, IResType> ALL_TYPES_MAP = new HashMap<String, IResType>();
	public static Map<String, EntityResType1> ALL_TYPE1S_MAP = new HashMap<String, EntityResType1>();
	public static Map<String, EntityResType2> ALL_TYPE2S_MAP = new HashMap<String, EntityResType2>();
	public static Map<String, EntityResType4> ALL_TYPE4S_MAP = new HashMap<String, EntityResType4>();
	
	private static GlobalResTypes instance = null;
	
	private DownloadHelper downloadHelperType2;
	private DownloadHelper downloadHelperType4;
	
	private GlobalResTypes() {
		downloadHelperType2 = new DownloadHelper(2);
		downloadHelperType4 = new DownloadHelper(2);
	}

	public static GlobalResTypes getInstance() {
		if (null == instance) {
			instance = new GlobalResTypes();
		}
		return instance;
	}
	
	/***********************************************************************************************************************************/
	/** 
	/** 课程图片缓存
	/** 
	/***********************************************************************************************************************************/
	
	private DownloadState preparelessonBitmapsState;
	private DownloadState lessonBitmapsState;
	private HashMap<String, Bitmap> resMap = new HashMap<String, Bitmap>();
	private List<LoadLessonImgParam> waitForLoadQueue = new ArrayList<LoadLessonImgParam>();
	private List<LoadLessonImgParam> currentDownloadingQueue = new ArrayList<LoadLessonImgParam>();
	
	public void prepareLoadLessonBitmaps(
			CallbackLessonRes callbackLessonRes, 
			List<LessonFragment> lessonFragments) {
		preparelessonBitmapsState = new DownloadState();
		new LoadBitmapTask(preparelessonBitmapsState, callbackLessonRes, lessonFragments).executeOnWaitNetwork();
	}
	
	public void displayLessonBitmap(String path, ILocalImg img) {
		lessonBitmapsState = new DownloadState();
		LoadLessonImgParam param = new LoadLessonImgParam(path, img);
		waitForLoadQueue.add(param);
		loadNext(false);
	}
	
	public void displayLessonBitmapNoCache(String path, ILocalImg img) {
		lessonBitmapsState = new DownloadState();
		LoadLessonImgParam param = new LoadLessonImgParam(path, img);
		waitForLoadQueue.add(param);
		loadNext(true);
	}
	
	public static interface ILocalImg {
		
		public void setBitmap(Bitmap bitmap);
		
		public void recycle();
		
		public float getImageWidth();
		
		public float getImageHeight();
		
	}
	
	public Bitmap getLessonBitmap(String path) {
		Bitmap bitmap = resMap.get(path);
		if (null != bitmap && bitmap.isRecycled()) {
			resMap.remove(path);
			bitmap = null;
		}
		return bitmap;
	}
	
	public void clearLessonBitmaps() {
		Bitmap bitmap = null;
		waitForLoadQueue.clear();
		currentDownloadingQueue.clear();
		for (String key : resMap.keySet()) {
			bitmap = resMap.get(key);
			if (null != bitmap) {
				bitmap.recycle();
			}
		}
		resMap.clear();
		if (null != lessonBitmapsState) {
			lessonBitmapsState.setAllowedTOdownload(false);
		}
		if (null != preparelessonBitmapsState) {
			preparelessonBitmapsState.setAllowedTOdownload(false);
		}
		System.gc();
	}
	
	public static interface CallbackLessonRes {
		
		public void onComplete();
		
	}
	
	private class LoadBitmapTask extends MyAsyncTask<Void, Bitmap, Void> {
		
		private DownloadState state;
		private List<LessonFragment> lessonFragments;
		private CallbackLessonRes callback;
		
		public LoadBitmapTask(
				DownloadState state,
				CallbackLessonRes callback, 
				List<LessonFragment> lessonFragments) {
			this.callback = callback;
			this.lessonFragments = lessonFragments;
			this.state = state;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bitmap = null;
			long time = System.currentTimeMillis();
			String type = null;
			String key = null;
			for (LessonFragment lessonFragment : lessonFragments) {
				for (LessonData data : lessonFragment.getDatas()) {
					if (!state.isAllowedTOdownload()) {
						return null;
					}
					type = data.getType().toUpperCase(Locale.getDefault());
					if (type.indexOf("01") != -1
							|| type.indexOf("02") != -1
							|| type.indexOf("03") != -1
							|| type.indexOf("07") != -1
							|| type.indexOf("10") != -1
							|| type.indexOf("11") != -1) {
						key = data.getAnswer().getLocalImgPath();
						bitmap = getLessonBitmap(key);
						if (null == bitmap) {
							bitmap = ImageUtils.readNormalPic(key, 0, 0);
							if (!state.isAllowedTOdownload()) {
								return null;
							}
							resMap.put(key, bitmap);
						}
					}
					else {
						for (Phrase phrase : data.getPhrases()) {
							key = phrase.getLocalImgPath();
							bitmap = getLessonBitmap(key);
							if (null == bitmap) {
								bitmap = ImageUtils.readNormalPic(key, 0, 0);
								if (!state.isAllowedTOdownload()) {
									return null;
								}
								resMap.put(key, bitmap);
							}
						}
					}
				}
			}
			DebugPrinter.e("加载耗时1：" + (System.currentTimeMillis() - time) + "ms");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void values) {
			super.onPostExecute(values);
			callback.onComplete();
		}
		
	}
	
	private void loadNext(boolean noCache) {
		if (currentDownloadingQueue.size() > 0 || waitForLoadQueue.size() == 0) {
			return;
		}
		LoadLessonImgParam param = waitForLoadQueue.remove(0);
		currentDownloadingQueue.add(param);
		new LoadSingleBitmapTask(lessonBitmapsState, param, noCache).executeOnWaitNetwork();
	}
	
	private class LoadSingleBitmapTask extends MyAsyncTask<Void, Bitmap, Bitmap> {
		
		private boolean noCache;
		private DownloadState state;
		private LoadLessonImgParam param;
		
		public LoadSingleBitmapTask(DownloadState state, LoadLessonImgParam param, boolean noCache) {
			this.state = state;
			this.param = param;
			this.noCache = noCache;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			long time = System.currentTimeMillis();
			if (!state.isAllowedTOdownload()) {
				return null;
			}
			Bitmap bitmap = null;
			bitmap = getLessonBitmap(param.path);
			DebugPrinter.e("加载耗时-2-1：" + (System.currentTimeMillis() - time) + "ms");
			if (null == bitmap) {
				time = System.currentTimeMillis();
				bitmap = ImageUtils.readNormalPic(param.path, (int) param.img.getImageWidth(), (int) param.img.getImageHeight());
				DebugPrinter.e("加载耗时-2-2：" + (System.currentTimeMillis() - time) + "ms");
				time = System.currentTimeMillis();
				if (!noCache) {
					resMap.put(param.path, bitmap);
				}
				DebugPrinter.e("加载耗时-2-3：" + (System.currentTimeMillis() - time) + "ms");
			}
			if (!state.isAllowedTOdownload()) {
				return bitmap;
			}
			return bitmap;
		}
		
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (null != result && !result.isRecycled()) {
				param.img.setBitmap(result);
			}
			if (currentDownloadingQueue.size() > 0) {
				currentDownloadingQueue.remove(0);
				loadNext(noCache);
			}
		}
	}
	
	private class LoadLessonImgParam {
		
		private String path;
		private ILocalImg img;
		
		public LoadLessonImgParam(String path, ILocalImg img) {
			this.path = path;
			this.img = img;
		}
		
	}
	
	/***********************************************************************************************************************************/
	/** 
	/** 单独资源文件加载
	/** 
	/***********************************************************************************************************************************/
	/**
	 * 获取资源包本地路径
	 * @param id
	 * @return
	 */
	private DownloadState downloadStateForPage;
	private String type5Id;
	
	private String typeDir;
	private String type5Url;
	
	public static Object LOCKED = new Object();
	
	
	/**
	 * 回调(流的方式)
	 * @author Jeffrey
	 *
	 */
	public static interface CallbackDownloadForPage {
		
		/**
		 * 每成一页 调用一次
		 * @param pageNo
		 */
		public void error(int pageNo);
		
		/**
		 * 每成一页 调用一次
		 * @param pageNo
		 */
		public void complete(int pageNo);
		
		/**
		 * 结束调用 
		 */
		public void finish(int pageNo);
		
		/**
		 * 配置文件加载完成
		 */
		public void configComplete(File file);
		
	}
	
	/**
	 * 开始下载(流的方式)
	 * @param type1Id
	 * @param type2Id
	 * @param type4Id
	 * @param type5Id
	 * @param callback
	 */
	public void startDownloadForPage(String type1Id, String type2Id, String type4Id, String type5Id, CallbackDownloadForPage callback) {
		stopDownloadForPage();
		this.type5Id = type5Id;
		EntityResType4 type4 = ALL_TYPE4S_MAP.get(type4Id);
		if (null == type4) {
			//没有该type4
			DebugPrinter.e("内存中没有该type4:" + type4Id + " type5:" + type5Id);
			callback.error(0);
			return;
		}
		typeDir = type4.getLocalTempDirPath();
		type5Url = ConfigConstants.RES_URL2 + type1Id + "/" + type2Id + "/" + type4Id + "/";
		downloadStateForPage = new DownloadState();
		new DownloadForPageAsyncTask(downloadStateForPage, callback).executeOnWaitNetwork();
	}
	
	/**
	 * 停止下载(流的方式)
	 */
	public void stopDownloadForPage() {
		if (null != downloadStateForPage) {
			downloadStateForPage.setAllowedTOdownload(false);
		}
		downloadStateForPage = null;
	}
	
	private class DownloadForPageAsyncTask extends MyAsyncTask<Void, Integer, Integer> {

		private DownloadState state;
		private Exception e;
		private File file = null;
		private String url;
		private QueueData currentData;
		private int errNum = 0;
		private CallbackDownloadForPage callback;
		
		private AtomicInteger loadType5Step;
		private AtomicInteger donwloadTaskFinishedNum;
		
		//当前完成页数
		private AtomicInteger finishedPageNo;
		//下载队列
		private ConcurrentLinkedQueue<QueueData> quene;
		
		//每页还需加载数量
		private SparseArray<AtomicInteger> pageNeedLoadedNum;
		
		
		public DownloadForPageAsyncTask(DownloadState state, CallbackDownloadForPage callback) {
			this.state = state;
			this.callback = callback;
			
			this.loadType5Step = new AtomicInteger(0);
			this.donwloadTaskFinishedNum = new AtomicInteger(0);
			this.finishedPageNo = new AtomicInteger(0);
			this.quene = new ConcurrentLinkedQueue<QueueData>();
			this.pageNeedLoadedNum = new SparseArray<AtomicInteger>();
		}
		
		public DownloadForPageAsyncTask(DownloadState state, CallbackDownloadForPage callback,
				AtomicInteger loadType5Step, AtomicInteger donwloadTaskFinishedNum, 
				AtomicInteger finishedPageNo, ConcurrentLinkedQueue<QueueData> quene, SparseArray<AtomicInteger> pageNeedLoadedNum) {
			this.state = state;
			this.callback = callback;
			
			this.loadType5Step = loadType5Step;
			this.donwloadTaskFinishedNum = donwloadTaskFinishedNum;
			this.finishedPageNo = finishedPageNo;
			this.quene = quene;
			this.pageNeedLoadedNum = pageNeedLoadedNum;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			String path = null;
			while (state.isAllowedTOdownload()) {
				switch (loadType5Step.get()) {
				//检查本地文件 (判断文件是否存在  存在:step2    不存在:step1)
				case 0:
					path = CacheDisk.getLessonPath(typeDir + type5Id + EntityResType5.FORMAT);
					file = new File(path);
					loadType5Step.set(file.exists() ? 2 : 1);
					continue;
				//下载type5.txt
				case 1:
					url = type5Id + EntityResType5.FORMAT;
					path = CacheDisk.getLessonPath(typeDir + type5Id + EntityResType5.FORMAT);
					file = FileUtils.createNewFileInSDCard(path);
					break;
				//解析type5.txt
				case 2:
					LessonFragment.parseTxt(file, typeDir);
					callback.configComplete(file);
					loadType5Step.set(3);
					path = CacheDisk.getLessonPath(typeDir + type5Id + EntityResType5.FORMAT);
					file = new File(path);
					if (!file.exists()) {
						loadType5Step.set(1);
						continue;
					}
					
					if (LessonFragment.LESSON_PAGES.size() == 0) {
						DebugPrinter.d("DownloadForPageAsyncTask 配置文件有无错请检查!");
						GlobalApp.getInstance().showErrMsg(GlobalApp.getInstance().getString(R.string.lessonResConfigErr));
						loadType5Step.set(1);
						file.delete();
						continue;
					}
					int size = 0;
					int pageNo = 0;
					for (List<LessonFragment> fragments : LessonFragment.LESSON_PAGES) {
						for (LessonFragment framgent : fragments) {
							for (LessonData data : framgent.getDatas()) {
								if ("07A".equals(data.getType())) {
									quene.add(new QueueData(data.getAnswer().getSoundUrl(), pageNo));
									size++;
									quene.add(new QueueData(data.getAnswer().getImgUrl(), pageNo));
									size++;
								}
								else if ("01D".equals(data.getType())) {
									quene.add(new QueueData(data.getPhrases().get(0).getImgUrl(), pageNo));
									size++;
								}
								else {
									for (Phrase phrase : data.getPhrases()) {
										quene.add(new QueueData(phrase.getSoundUrl(), pageNo));
										size++;
										quene.add(new QueueData(phrase.getImgUrl(), pageNo));
										size++;
									}
								}
							}
						}
						pageNeedLoadedNum.append(pageNo++, new AtomicInteger(size));
						size = 0;
					}
					continue;
				//开启多线程下载
				case 3:
					loadType5Step.set(4);
//					new DownloadForPageAsyncTask(state, callback, loadType5Step, donwloadTaskFinishedNum, finishedPageNo, quene, pageNeedLoadedNum).executeOnWaitNetwork();
//					new DownloadForPageAsyncTask(state, callback, loadType5Step, donwloadTaskFinishedNum, finishedPageNo, quene, pageNeedLoadedNum).executeOnWaitNetwork();
					continue;
				//队列加载内容文件
				case 4:
					if (errNum == 0) {
						if (quene.size() == 0) {
							loadType5Step.set(5);
							continue;
						}
						currentData = quene.poll();
						url = currentData.url;
					}
					path = CacheDisk.getLessonPath(typeDir + url);
					file = new File(path);
					if (file.exists()) {
						//TODO:检查文件完整性
						if (file.length() > 1024) {
							checkPage();
							continue;
						}
						file.delete();
						file = new File(path);
					}
					try {
						File dir = file.getParentFile();
						if (!dir.exists())
							dir.mkdirs();
						if (!file.createNewFile()) {
							DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败");
						}
					} catch (IOException e) {
						DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败" + e.getMessage());
						GlobalApp.getInstance().showErrMsg(GlobalApp.getInstance().getString(R.string.sd_card_in_not_mounted));
						loadType5Step.set(6);
						continue;
					}
					break;
				// finish
				case 5:
					return finishedPageNo.get();
				case 6:
					state.setAllowedTOdownload(false);
					return null;
				}
				
				HttpReqFactory.createDownloadFileReq(
					new HttpReqHelper.DownloadFileListener() {

						@Override
						public void startHandler(long max) {
						}
						
						@Override
						public void progressHandler(long progress, long max) {
						}

						@Override
						public void errorHandler(Exception e, String errMsg) {
							//失败重新加载
							DownloadForPageAsyncTask.this.e = e;
							if (file.exists()) {
								file.delete();
							}
							if (errNum++ > 2) {
								errNum = 0;
//								loadType5Step.set(6);
								return;
							}
						}

						@Override
						public void completeHandler(File file) {
							//下载完成type5.txt 执行下一步
							if (loadType5Step.get() == 1) {
								loadType5Step.set(2);
								return;
							}
							//成功加载下一个  判断是否完成一页  完成则通知调用者
							checkPage();
						}

						@Override
						public void stopHandler(long progress, long max1) {
							//停止删除文件
							if (file.exists())
								file.delete();
						}
						
					}, type5Url + url, state, file);
			}
			return finishedPageNo.get();
		}
		
		private void checkPage() {
			Integer pagetNo = currentData.pageNo;
			if (null == pagetNo) {
				return;
			}
			AtomicInteger i = pageNeedLoadedNum.get(pagetNo);
			if (i.decrementAndGet() == 0) {
				synchronized (LOCKED) {
					publishProgress(finishedPageNo.get());
					finishedPageNo.addAndGet(1);
				}
				return;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (null == callback) {
				return;
			}
			donwloadTaskFinishedNum.addAndGet(1);
			if (donwloadTaskFinishedNum.get() < 1) {
				return;
			}
			if (null == result) {
				callback.error(finishedPageNo.get());
			}
			else {
				callback.complete(result);
				callback.finish(result);
			}
			state.setAllowedTOdownload(false);
			callback = null;
			state = null;
			e = null;
			file = null;
			url = null;
			loadType5Step = null;
			donwloadTaskFinishedNum = null;
			finishedPageNo = null;
			quene.clear();
			quene = null;
			pageNeedLoadedNum.clear();
			pageNeedLoadedNum = null;
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (null == callback) {
				return;
			}
			callback.complete(values[0]);
		}
		
	}
	
	private class QueueData {
		
		public QueueData(String url, int pageNo) {
			this.url = url;
			this.pageNo = pageNo;
		}
		
		public QueueData(Long key, String url, int pageNo) {
			this.key = key;
			this.url = url;
			this.pageNo = pageNo;
		}
		
		private Long key;
		private String url;
		private int pageNo;
		
	}
	
	/***********************************************************************************************************************************/
	/** 
	/** 资源包下载
	/** 
	/***********************************************************************************************************************************/
	
	/**
	 * 判断资源是否被完整下载
	 * @param id
	 * @return
	 */
	public boolean isDownloaded(String id) {
		IResType entity = ALL_TYPES_MAP.get(id);
		if (null == entity) {
			return false;
		}
		return entity.isFinished();
	}
	
	/**
	 * 获取资源包
	 * @param id
	 * @return
	 */
	public File getResFile(String id) {
		IResType entity = ALL_TYPES_MAP.get(id);
		if (null == entity || !entity.isFinished()) {
			return null;
		}
		File file = entity.getLocalFile();
		if (!file.exists()) {
			return null;
		}
		return entity.getLocalFile();
	}
	
	/**
	 * 获取资源包本地路径
	 * @param id
	 * @return
	 */
	public String getResFilePath(String id) {
		File file = getResFile(id);
		if (null == file) {
			return null;
		}
		return file.getAbsolutePath();
	}
	
	/**
	 * 开始下载
	 * @param id
	 * @return
	 */
	public boolean startDownload(String id) {
		IResType entity = ALL_TYPES_MAP.get(id);
		if (null == entity) {
			DebugPrinter.e("数据错误 startDownload id :" + id + "找不到数据!");
			return false;
		}
		if (entity.isFinished()) {
			DebugPrinter.i("startDownload id :" + id + "已下载完!");
			return true;
		}
		startDownload(entity);
		return true;
	}
	
	public int getType4QueueSize() {
		return downloadHelperType4.getQueueSize();
	}
	
	public int getType2QueueSize() {
		return downloadHelperType2.getQueueSize();
	}
	
	private void call(Context context ,boolean state, List<String> typeIds) {
		// 发送自定义广播
		Intent intent = new Intent();
//		intent.putExtra("typeId", id);
		intent.putStringArrayListExtra("typeIds", (ArrayList<String>)typeIds);
		intent.putExtra("state", state);
		intent.setAction("com.shuangge.english.support.service.Type4DownLoadService.MyReciver");
		context.sendBroadcast(intent);
	}
	
	public boolean startDownloadWithService(Context context ,final List<String> ids) {
		final List<String> typeIds = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			IResType entity = ALL_TYPES_MAP.get(ids.get(i));
			if (null == entity) {
				DebugPrinter.e("数据错误 startDownload id :" + ids.get(i) + "找不到数据!");
				continue;
			}
			if (entity.isFinished()) {
				DebugPrinter.i("startDownload id :" + ids.get(i) + "已下载完!");
				continue;
			}
			typeIds.add(ids.get(i));
		}
		
		if (ServiceUtils.checkService(context, Type4DownLoadService.class,
				new ServiceIntentCallBack() {
			@Override
			public void setServiceIntent(Intent intent) {
				intent.putStringArrayListExtra("typeIds", (ArrayList<String>) typeIds);
				intent.putExtra("state", true);
			}
		})) {
			call(context, true, typeIds);
		}
		return true;
	}
	
	public boolean startDownload(IResType entity) {
		if (entity instanceof EntityResType2) {
			if (downloadHelperType2.isQueueDownloading(entity.getUrl())) {
				return true;
			}
			File file = entity.getLocalFile();
			long fileLength = 0;
			if (file.exists()) {
				fileLength = file.length();
			}
			DebugPrinter.i("start download : " + entity.getUrl());
			DownloadState state = new DownloadState(true, fileLength, new CallbackDownloadState() {
				
				@Override
				public void startInBackground(Object param, long fileLength) {
					String id = (String) param;
					IResType entity = ALL_TYPES_MAP.get(id);
					if (null == entity) {
						return;
					}
					entity.setMax(fileLength);
					entity.setStatus(IResType.STATUS_START);
					if (entity instanceof EntityResType2) {
						DaoResType2 dao2 = new DaoResType2();
						EntityResType2 type2 = (EntityResType2) entity;
						dao2.update(type2.getId(), type2);
						return;
					}
					DebugPrinter.i("startInBackground : " + entity.getUrl());
				}
				
				@Override
				public void finishInBackground(Object param) {
					String id = (String) param;
					IResType entity = ALL_TYPES_MAP.get(id);
					if (null == entity) {
						return;
					}
					entity.setStatus(IResType.STATUS_FINISH);
					if (entity instanceof EntityResType2) {
						EntityResType2 type2 = (EntityResType2) entity;
						parseType2Zip(type2);
						return;
					}
				}

				@Override
				public void onError(Exception e, String errorMsg) {
				}
				
			}, entity.getId());
			state.setFilePath(entity.getLocalPath());
			downloadHelperType2.startQueueDownload(entity.getUrl(), state);
		}
		else if (entity instanceof EntityResType4) {
			if (downloadHelperType4.isQueueDownloading(entity.getUrl())) {
				return true;
			}
			File file = entity.getLocalFile();
			long fileLength = 0;
			if (file.exists()) {
				fileLength = file.length();
			}
			DownloadState state = new DownloadState(true, fileLength, new CallbackDownloadState() {
				
				@Override
				public void startInBackground(Object param, long fileLength) {
					String id = (String) param;
					IResType entity = ALL_TYPES_MAP.get(id);
					if (null == entity) {
						return;
					}
					entity.setMax(fileLength);
					entity.setStatus(IResType.STATUS_START);
					if (entity instanceof EntityResType4) {
						DaoResType4 dao4 = new DaoResType4();
						EntityResType4 type4 = (EntityResType4) entity;
						dao4.update(type4.getId(), type4);
						return;
					}
				}
				
				@Override
				public void finishInBackground(Object param) {
					String id = (String) param;
					IResType entity = ALL_TYPES_MAP.get(id);
					if (null == entity) {
						return;
					}
					entity.setStatus(IResType.STATUS_FINISH);
					if (entity instanceof EntityResType4) {
						DaoResType4 dao4 = new DaoResType4();
						EntityResType4 type4 = (EntityResType4) entity;
						dao4.update(type4.getId(), type4);
						
						//TODO:文件解压
						File type4Dir = type4.getLocalTempDir();
						//删除之前所有临时文件
						FileUtils.deleteDirectory(type4Dir);
						
						File file = type4.getLocalFile();
						//异常
						if (!file.exists()) {
							return;
						}
						ZipUtils.uncompression(type4.getLocalPath(), type4Dir.getAbsolutePath() + File.separator);
						return;
					}
				}

				@Override
				public void onError(Exception e, String errorMsg) {
				}
				
			}, entity.getId());
			state.setFilePath(entity.getLocalPath());
			downloadHelperType4.startQueueDownload(entity.getUrl(), state);
		}
		return true;
	}
	
	public static void parseType2Zip(EntityResType2 type2) {
		long time = System.currentTimeMillis();
		String str = ZipUtils.getTextFromZipFile(type2.getLocalPath(), "lessonData.txt");
		if (null == str) {
			return;
		}
//		DaoResType2 dao2 = new DaoResType2();
//		dao2.update(type2.getId(), type2);
		
		EntityResType4 type4 = null;
		EntityResType5 type5 = null;
		String[] strs = str.split("\n");
		DaoResType4 dao4 = new DaoResType4();
		dao4.deleteFromParentId(type2.getId());
		DebugPrinter.d(dao4.getByParentId(type2.getId()).size() + "");
		
		DebugPrinter.i("解压 lessonData.txt 耗时 :" + (System.currentTimeMillis() - time) + "ms");
		time = System.currentTimeMillis(); 
		
		String prevId = null;
		String url = null;
		for (int i = 1; i < strs.length; i++) {
			String[] arr = strs[i].split("\t");
			String pageId = arr.length > 9 ? arr[8] : null;
			String resId = arr.length > 9 ? arr[9] : null;
			if (!arr[2].equals(prevId)) {
				//type4 保存
				if (null != type4)
					dao4.update(type4.getId(), type4);
				url = ConfigConstants.RES_URL + type2.getParentId() + "/" + type2.getId() + "/" + arr[2] + EntityResType4.FORMAT;
				type4 = new EntityResType4(arr[2], type2.getId(), arr[3], url, Double.parseDouble(arr[7]));
				type4.setCore(Integer.valueOf(arr[6]) == 2);
				type4 = (EntityResType4) TaskCheckCatalog.parseType4Data(type4);
			}
			//type5保存
			type5 = new EntityResType5(arr[4], arr[2], arr[5], pageId, resId);
			type4.getType5s().add(type5);
			
			prevId = arr[2];
		}
		//type4 保存
		if (null != type4)
			dao4.update(type4.getId(), type4);
		DebugPrinter.i("存储type4 type5 耗时 :" + (System.currentTimeMillis() - time) + "ms");
		DebugPrinter.i("finishInBackground : " + type2.getUrl());
	}
	
	
	public boolean stopDownloadWithService(Context context, List<String> ids) {
		
		List<String> typeIds = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			IResType entity = ALL_TYPES_MAP.get(ids.get(i));
			if (null == entity) {
				continue;
			}
			typeIds.add(ids.get(i));
		}
		
		call(context, false, typeIds);
		
		return false;
	}
	
	/**
	 * 停止下载
	 * @param id
	 * @return
	 */
	public boolean stopDownload(String id) {
		IResType entity = ALL_TYPES_MAP.get(id);
		if (null == entity) {
			return false;
		}
		if (entity instanceof EntityResType2) {
			downloadHelperType2.stopQueueDownload(entity.getUrl());
			return true;
		}
		if (entity instanceof EntityResType4) {
			downloadHelperType4.stopQueueDownload(entity.getUrl());
			return true;
		}
		return false;
	}
	
	public boolean stopAllQueueDownload() {
		downloadHelperType4.stopAllQueueDownload();
		return true;
	}
	
	/***********************************************************************************************************************************/
	/** 
	/** 回调
	/** 
	/***********************************************************************************************************************************/
	
	public void setCallBackType2(CallbackResDownload callback) {
		downloadHelperType2.setCallBackQueueDownload(new CallbackResDownloadImpl(callback));
	}
	
	public void clearCallbackType2() {
		downloadHelperType2.clearCallBackQueueDownload();
	}
	
	public void setCallBackType4(CallbackResDownload callback) {
		downloadHelperType4.setCallBackQueueDownload(new CallbackResDownloadImpl(callback));
	}
	
	public void clearCallbackType4() {
		downloadHelperType4.clearCallBackQueueDownload();
	}
	
	public static interface CallbackResDownload {
		
		public void startHandler(String id, long max);
		
		public void waitingHanler(String id);
	
		public void progressHandler(String id, long progress, long max);
		
		public void finishedHandler(String id);
		
        public void errorHandler(String id);
        
        public void stopHandler(String id, long progress, long max);
		
	}
	
	private static class CallbackResDownloadImpl implements DownloadHelper.CallbackDownload {
		
		private CallbackResDownload callback;
		
		public CallbackResDownloadImpl(CallbackResDownload callback) {
			this.callback = callback;
		}
		
		@Override
		public void waitingHanler(String url) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			entity.setStatus(EntityResType4.STATUS_WAIT);
			callback.waitingHanler(getIdByUrl(url));
		}
		
		@Override
		public void stopHandler(String url, long progress, long max) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			entity.setProgress(progress);
			entity.setMax(max);
			entity.setStatus(EntityResType4.STATUS_STOP);
			callback.stopHandler(getIdByUrl(url), progress, max);
		}
		
		@Override
		public void startHandler(String url, long max) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			entity.setMax(max);
			entity.setStatus(EntityResType4.STATUS_START);
			callback.startHandler(getIdByUrl(url), max);
		}
		
		@Override
		public void progressHandler(String url, long progress, long max) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			if (entity.getStatus() != EntityResType4.STATUS_START) {
				return;
			}
			entity.setProgress(progress);
			entity.setStatus(EntityResType4.STATUS_START);
			callback.progressHandler(getIdByUrl(url), progress, max);
		}
		
		@Override
		public void finishedHandler(String url) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			entity.setStatus(EntityResType4.STATUS_FINISH);
			callback.finishedHandler(getIdByUrl(url));
		}
		
		@Override
		public void errorHandler(String url) {
			String id = getIdByUrl(url);
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
			if (null == entity) {
				return;
			}
			entity.setProgress(0);
			entity.setStatus(EntityResType4.STATUS_STOP);
			callback.errorHandler(getIdByUrl(url));
		}
		
	}
	
	private static String getIdByUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.length() - EntityResType4.FORMAT.length());
	}
	
	/*********************************************************************************************************************************************/
	/**
	/** 阅读里的课程
	/**
	/*********************************************************************************************************************************************/
	
	private IWord[] words;
	
	/**
	 * 开始下载(流的方式)
	 * @param type1Id
	 * @param type2Id
	 * @param type4Id
	 * @param type5Id
	 * @param callback
	 */
	public void startDownloadForWords(IWord[] words, CallbackDownloadForPage callback) {
		stopDownloadForPage();
		this.words = words;
		downloadStateForPage = new DownloadState();
		new DownloadForWordsAsyncTask(downloadStateForPage, callback).executeOnWaitNetwork();
	}
	
	/**
	 * 停止下载(流的方式)
	 */
	public void stopDownloadForWords() {
		if (null != downloadStateForPage) {
			downloadStateForPage.setAllowedTOdownload(false);
		}
		downloadStateForPage = null;
	}
	
	private class DownloadForWordsAsyncTask extends MyAsyncTask<Void, Integer, Integer> {

		private DownloadState state;
		private Exception e;
		private File file = null;
		private String url;
		private QueueData currentData;
		private int errNum = 0;
		private CallbackDownloadForPage callback;
		
		private AtomicInteger loadType5Step;
		private AtomicInteger donwloadTaskFinishedNum;
		
		//当前完成页数
		private AtomicInteger finishedPageNo;
		//下载队列
		private ConcurrentLinkedQueue<QueueData> quene;
		//每页还需加载数量
		private SparseArray<AtomicInteger> pageNeedLoadedNum;
		
		
		public DownloadForWordsAsyncTask(DownloadState state, CallbackDownloadForPage callback) {
			this.state = state;
			this.callback = callback;
			
			this.loadType5Step = new AtomicInteger(0);
			this.donwloadTaskFinishedNum = new AtomicInteger(0);
			this.finishedPageNo = new AtomicInteger(0);
			this.quene = new ConcurrentLinkedQueue<QueueData>();
			this.pageNeedLoadedNum = new SparseArray<AtomicInteger>();
		}
		
		public DownloadForWordsAsyncTask(DownloadState state, CallbackDownloadForPage callback,
				AtomicInteger loadType5Step, AtomicInteger donwloadTaskFinishedNum, 
				AtomicInteger finishedPageNo, ConcurrentLinkedQueue<QueueData> quene, SparseArray<AtomicInteger> pageNeedLoadedNum) {
			this.state = state;
			this.callback = callback;
			
			this.loadType5Step = loadType5Step;
			this.donwloadTaskFinishedNum = donwloadTaskFinishedNum;
			this.finishedPageNo = finishedPageNo;
			this.quene = quene;
			this.pageNeedLoadedNum = pageNeedLoadedNum;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			String path = null;
			while (state.isAllowedTOdownload()) {
				switch (loadType5Step.get()) {
				//检查本地文件 (判断文件是否存在  存在:step2    不存在:step1)
				case 0:
					int pageNo = 0;
					for (IWord word : words) {
						quene.add(new QueueData(word.getId(), word.getImgUrl(), pageNo));
						quene.add(new QueueData(word.getId(), word.getSoundUrl(), pageNo));
						pageNeedLoadedNum.append(pageNo++, new AtomicInteger(2));
					}
					loadType5Step.set(3);
					continue;
				//开启多线程下载
				case 3:
					loadType5Step.set(4);
//					new DownloadForWordsAsyncTask(state, callback, loadType5Step, donwloadTaskFinishedNum, finishedPageNo, quene, pageNeedLoadedNum).executeOnWaitNetwork();
//					new DownloadForWordsAsyncTask(state, callback, loadType5Step, donwloadTaskFinishedNum, finishedPageNo, quene, pageNeedLoadedNum).executeOnWaitNetwork();
					continue;
				//队列加载内容文件
				case 4:
					if (errNum == 0) {
						if (quene.size() == 0) {
							loadType5Step.set(5);
							continue;
						}
						currentData = quene.poll();
						url = currentData.url;
					}
					path = CacheDisk.getWordPath() + currentData.key + url.substring(url.lastIndexOf("."));
					file = new File(path);
					if (file.exists()) {
						//TODO:检查文件完整性
						if (file.length() > 1024) {
							checkPage();
							continue;
						}
						file.delete();
						file = new File(path);
					}
					try {
						File dir = file.getParentFile();
						if (!dir.exists())
							dir.mkdirs();
						if (!file.createNewFile()) {
							DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败");
						}
					} catch (IOException e) {
						DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败" + e.getMessage());
						loadType5Step.set(6);
						continue;
					}
					break;
				// finish
				case 5:
					return finishedPageNo.get();
				case 6:
					state.setAllowedTOdownload(false);
					return null;
				}
				
				HttpReqFactory.createDownloadFileReq(
					new HttpReqHelper.DownloadFileListener() {

						@Override
						public void startHandler(long max) {
						}
						
						@Override
						public void progressHandler(long progress, long max) {
						}

						@Override
						public void errorHandler(Exception e, String errMsg) {
							//失败重新加载
							DownloadForWordsAsyncTask.this.e = e;
							if (file.exists()) {
								file.delete();
							}
							if (errNum++ > 2) {
								errNum = 0;
								checkPage();   //下载失败两次 当作做完成处理 使用默认图片和音频
//								loadType5Step.set(6);
								return;
							}
						}

						@Override
						public void completeHandler(File file) {
							//下载完成type5.txt 执行下一步
							if (loadType5Step.get() == 1) {
								loadType5Step.set(2);
								return;
							}
							//成功加载下一个  判断是否完成一页  完成则通知调用者
							checkPage();
						}

						@Override
						public void stopHandler(long progress, long max1) {
							//停止删除文件
							if (file.exists())
								file.delete();
						}
						
					}, url, state, file);
			}
			return finishedPageNo.get();
		}
		
		private void checkPage() {
			Integer pagetNo = currentData.pageNo;
			if (null == pagetNo) {
				return;
			}
			AtomicInteger i = pageNeedLoadedNum.get(pagetNo);
			if (i.decrementAndGet() == 0) {
				synchronized (LOCKED) {
					publishProgress(finishedPageNo.get());
					finishedPageNo.addAndGet(1);
				}
				return;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (null == callback) {
				return;
			}
			donwloadTaskFinishedNum.addAndGet(1);
			if (donwloadTaskFinishedNum.get() < 1) {
				return;
			}
			if (null == result) {
				callback.error(finishedPageNo.get());
			}
			else {
				callback.complete(result);
				callback.finish(result);
			}
			state.setAllowedTOdownload(false);
			callback = null;
			state = null;
			e = null;
			file = null;
			url = null;
			loadType5Step = null;
			donwloadTaskFinishedNum = null;
			finishedPageNo = null;
			quene.clear();
			quene = null;
			pageNeedLoadedNum.clear();
			pageNeedLoadedNum = null;
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (null == callback) {
				return;
			}
			callback.complete(values[0]);
		}
		
	}
	
	
}
