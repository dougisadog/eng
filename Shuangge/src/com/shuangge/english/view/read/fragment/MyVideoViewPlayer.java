package com.shuangge.english.view.read.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.SoundUtils;
import com.shuangge.english.view.read.AtyVideoTest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MyVideoViewPlayer {

	private Context context;
	
	private VideoView videoview , originVideoview;
	
	private ProgressDialog pDialog;
	
	private VideoCallBack videoCallBack;
	
	private Map<Integer, Integer> mapDatas;
	
	private String url;
	
	public interface VideoCallBack {
		void prepare4Video();
		
		void refreshProgressText(String text);
		
		void progressUpdate(int progress);
		
		void onVideoCompleted();
	}

	public MyVideoViewPlayer (Context context , final VideoView videoview) {
		this.context = context;
		this.videoview = videoview;
		this.originVideoview = videoview;
		
	}
	
	public MyVideoViewPlayer (Context context , final VideoView videoview, VideoCallBack videoCallBack) {
		this.context = context;
		this.videoview = videoview;
		this.originVideoview = videoview;
		this.videoCallBack = videoCallBack;
	}
	
	public MyVideoViewPlayer (Context context , final VideoView videoview, VideoCallBack videoCallBack, Map<Integer, Integer> mapDatas) {
		this.context = context;
		this.videoview = videoview;
		this.originVideoview = videoview;
		this.videoCallBack = videoCallBack;
		this.mapDatas = mapDatas;
	}
	
	public void initVideo(String url) {
//		if (url.equals(url)) return;
//		this.url = url; = 
		videoview = originVideoview;
		loadVideoRes(url);
		initVideoView(url);
	}
	
	private void initDialog(String title, String content) {
		if (null == pDialog) {
			pDialog = new ProgressDialog(context);
			// Set progressbar title
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						pDialog.dismiss();
					}
					return false;
				}
			});
		}
		pDialog.setTitle(title);
		// Set progressbar message
		pDialog.setMessage(content);
		pDialog.show();
	}
	
	private void initVideoView(final String videoURL) {
		
		videoview.requestFocus();
		duration = videoview.getDuration();
		videoview.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				dismissProgressDialog();
				duration = videoview.getDuration();
				//判断缓存替换 使用记录的 时间位置
				if (iserror) {
					videoview.seekTo(curPosition);
				}
//				videoview.start();
				if (null != mapDatas && mapDatas.size() > 0)
				listenToVideo();
				videoCallBack.prepare4Video();
				videoview.start();
			}
		});
       
	    videoview.setOnCompletionListener(new OnCompletionListener() {

	        public void onCompletion(MediaPlayer mediaplayer) {
//	            curPosition = 0;
	            videoview.pause();
	            videoCallBack.onVideoCompleted();
	        }
	    });

	    videoview.setOnErrorListener(new OnErrorListener() {

	        public boolean onError(MediaPlayer mediaplayer, int err, int extra) {
//	        	alive = false;
//	        	curPosition = videoview.getCurrentPosition();
	        	iserror = true;
	        	//普通等待网络 弹出dialog
	        	if (!cacheFinished) {
	        		videoview.pause();
	        		showProgressDialog();
	        	}
	        	//旧缓存 不完全 使用 完全缓存替换
	        	else {
	        		videoview.setVideoPath(localUrl);
	        		videoview.start();
	        	}
	            return true;
	        }
	    });
//		final String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
//		final String videoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421746.mp4";
		try {
			// Start the MediaController
			MediaController mediacontroller = new MediaController(context);
			mediacontroller.setAnchorView(videoview);
			// Get the URL from String VideoURL
			String name = "test2";
			name = SoundUtils.getFileNameByUrl(videoURL);
			if (new File(CacheDisk.getSoundDir() + "/" + name).exists()) {
				videoview.setVideoPath(CacheDisk.getSoundDir() + "/" + name);
			}
			else {
				Uri video = Uri.parse(videoURL);
				videoview.setVideoURI(video);
			}
			videoview.setMediaController(mediacontroller);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static final int READY_BUFF = 2000 * 1024;  //预缓存 大小
	private static final int CACHE_BUFF = 500 * 1024;   //播放中 核心缓存 大小
	private boolean isready = false;					//预缓存 是否加载完成
	private boolean iserror = false;					//视频播放 是否网络 IO 错误
	private int curPosition = 0;
	private long mediaLength = 0;						//视频总大小
	private long readSize = 0;							//已下载大小
	private String localUrl;  							//本地缓存地址
	
	private int duration = 0;  							//加载视频总长
	
	private boolean alive = false; 						// TheadVideoTestCheck 线程 存活状态
	private boolean cacheFinished = false; 				//缓存是否完全下载完成
	
	public void setAtyVideoTestAlive(boolean alive) {
		this.alive = alive;
	}
	/**
	 * 开启线程追踪 视频播放
	 *
	 */
	public class TheadVideoTestCheck extends BaseTask<Object, Integer, Boolean> {
		 
	     int current = 0;
	        
		public TheadVideoTestCheck(int tag, CallbackNoticeView<Integer, Boolean> callback, Object... params) {
			super(tag, callback, params);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
//			System.out.println(values[0]);
			//根据发布的时间节点  更新UI
			if (null == mapDatas || mapDatas.size() == 0)
				return;
			if (mapDatas.containsKey(values[0])) {
//				Toast.makeText(AtyVideoTest.this, values[0] + "", Toast.LENGTH_SHORT).show();
//				vp.setCurrentItem(mapDatas.get(values[0]));
				videoCallBack.progressUpdate(values[0]);
				mapDatas.remove(values[0]);
//				videoview.pause();
			}
			
			super.onProgressUpdate(values);
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			if (null == mapDatas)
				return true;
            try {
				do {
				    current = videoview.getCurrentPosition();
//				    System.out.println("duration - " + duration + " current- " + current);
				    //发布 预定时间节点 
				    if (mapDatas.containsKey(current/1000) && videoview.isPlaying()) {
				    	publishProgress(current/1000);
					}
				} while (current/1000 < duration/1000 && alive && mapDatas.size() > 0 ); //条件 可改为 最后的停顿点时间
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
		
	}
	
	public void startPlay() {
		videoview.start();
	}
	
	public void pausePlay() {
		videoview.pause();
	}
	
	public void seekTo(int msec) {
		videoview.seekTo(msec);
	}
	
	private void showProgressDialog() {
	    mHandler.post(new Runnable() {

	        @Override
	        public void run() {
	            if (pDialog == null) {
	            	initDialog("视频缓存", "正在努力加载中 ...");
	            }
	        }
	    });
	}

	private void dismissProgressDialog() {
	    mHandler.post(new Runnable() {

	        @Override
	        public void run() {
	            if (pDialog != null) {
	                pDialog.dismiss();
	                pDialog = null;
	            }
	        }
	    });
	}
	
	/**
	 * 开启视频监听线程
	 */
	private void listenToVideo() {
		 if (!alive) {
		    	alive = true;
		    	new TheadVideoTestCheck(0, new CallbackNoticeView<Integer, Boolean>() {
		    		
		    		@Override
		    		public void refreshView(int tag, Boolean result) {
		    			if (result) {
		    				alive = false;
		    			}
		    		}
		    		
		    		@Override
		    		public void onProgressUpdate(int tag, Integer[] values) {
		    		}
		    		
		    	});
		    }
	}
	
	private void loadVideoRes(final String remoteUrl) {
		showProgressDialog();
	    new Thread(new Runnable() {

	        @Override
	        public void run() {
	            FileOutputStream out = null;
	            InputStream is = null;

	            try {
	                URL url = new URL(remoteUrl);
	                HttpURLConnection httpConnection = (HttpURLConnection) url
	                        .openConnection();

	                if (localUrl == null) {
	                	String name = "test2.mp4";
	                	name = SoundUtils.getFileNameByUrl(remoteUrl);
	                    localUrl = CacheDisk.getSoundDir() + "/" + name;
	                }

	                System.out.println("localUrl: " + localUrl);

	                File cacheFile = new File(localUrl);

	                if (!cacheFile.exists()) {
	                    cacheFile.getParentFile().mkdirs();
	                    cacheFile.createNewFile();
	                }

	                readSize = cacheFile.length();
	                out = new FileOutputStream(cacheFile, true);

	                httpConnection.setRequestProperty("User-Agent", "NetFox");
	                httpConnection.setRequestProperty("RANGE", "bytes="
	                        + readSize + "-");

	                is = httpConnection.getInputStream();

	                mediaLength = httpConnection.getContentLength();
	                if (mediaLength == -1) {
	                    return;
	                }

	                mediaLength += readSize;

	                byte buf[] = new byte[8 * 1024];
	                int size = 0;
	                long lastReadSize = 0;
	                mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE);

	                while ((size = is.read(buf)) != -1) {
	                    try {
	                        out.write(buf, 0, size);
	                        readSize += size;
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }

	                    if (!isready) {
	                        if ((readSize - lastReadSize) > READY_BUFF) {
	                            lastReadSize = readSize;
	                            mHandler.sendEmptyMessage(CACHE_VIDEO_READY);
	                        }
	                    } else {
	                        if ((readSize - lastReadSize) > CACHE_BUFF) {
	                            lastReadSize = readSize;
	                            mHandler.sendEmptyMessage(CACHE_VIDEO_UPDATE);
	                        }
	                    }
	                }
	                mHandler.sendEmptyMessage(CACHE_VIDEO_END);
	            } catch (Exception e) {
	            	dismissProgressDialog();
	            	videoview.pause();
	                e.printStackTrace();
	            } finally {
	                if (out != null) {
	                    try {
	                        out.close();
	                    } catch (IOException e) {
	                        //
	                    }
	                }

	                if (is != null) {
	                    try {
	                        is.close();
	                    } catch (IOException e) {
	                        //
	                    }
	                }
	            }

	        }
	    }).start();

	}
	
	private final static int VIDEO_STATE_UPDATE = 0;
	private final static int CACHE_VIDEO_READY = 1;
	private final static int CACHE_VIDEO_UPDATE = 2;
	private final static int CACHE_VIDEO_END = 3;

	/**
	 * 视频读取/下载 事件处理
	 */
	private final Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case VIDEO_STATE_UPDATE:
	            double cachepercent = readSize * 100.00 / mediaLength * 1.0;
	            String s = String.format("已缓存: [%.2f%%]", cachepercent);

	            if (videoview.isPlaying()) {
	                curPosition = videoview.getCurrentPosition();
	                int duration = videoview.getDuration();
	                duration = duration == 0 ? 1 : duration;

	                double playpercent = curPosition * 100.00 / duration * 1.0;

	                int i = curPosition / 1000;
	                int hour = i / (60 * 60);
	                int minute = i / 60 % 60;
	                int second = i % 60;

	                s += String.format(" 播放: %02d:%02d:%02d [%.2f%%]", hour,
	                        minute, second, playpercent);
	            }

//	            tvcache.setText(s);
//	            txtBarTitleCn.setText(s);
	            videoCallBack.refreshProgressText(s);
	            mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
	            break;

	        case CACHE_VIDEO_READY: //初始预缓存 加载完成
	            isready = true;
	            videoview.setVideoPath(localUrl);
	            videoview.start();
	            break;

	        case CACHE_VIDEO_UPDATE: //播放中 核心缓存 加载完成
	            if (iserror) {
	                videoview.setVideoPath(localUrl);
	                videoview.start();
	                iserror = false;
	            }
	            break;

	        case CACHE_VIDEO_END: //缓存 完全完成
	        	curPosition = videoview.getCurrentPosition();
	            cacheFinished = true;
	            break;
	        }

	        super.handleMessage(msg);
	    }
	};
}
