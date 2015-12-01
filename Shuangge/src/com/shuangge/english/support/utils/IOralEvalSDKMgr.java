package com.shuangge.english.support.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.text.TextUtils;
import cn.yunzhisheng.oraleval.sdk.IOralEvalSDK;
import cn.yunzhisheng.oraleval.sdk.OralEvalSDKFactory;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 语音识别 管理器
 * @author Jeffrey
 *
 */
public class IOralEvalSDKMgr {
	
	private static final boolean USE_OFFLINE_SDK_IF_FAIL_TO_SERVER = false;
	
	public static final int TIME_OUT1 = 600;
	public static final int TIME_OUT2 = 1200;

	private static IOralEvalSDKMgr instance;

	public static IOralEvalSDKMgr getInstance() {
		if (null == instance) {
			instance = new IOralEvalSDKMgr();
		}
		return instance;
	}
	
	private IOralEvalSDKMgr() {
	}

	private Activity context;
	private IOralEvalSDK _oe;
	private CallbackIOralEvalSDK callback;
	private String content;
	private Long firstVoiceTimer;
	private Long lastVoiceTimer;
	private Long noVoiceTimer;
	
	//声音回放
	private ByteArrayOutputStream _pcmBuf;
	private AudioTrack _at;
	private OutputStream _record;
	private boolean isFinished = false;
	
	private int count = 1;
	
	
	public static interface CallbackIOralEvalSDK {
		
		public void onResult(boolean pass, int score, String htmlStr);
		
		public void onVoice(int num);
		
		public void onError(IOralEvalSDK.Error error, IOralEvalSDK.OfflineSDKPreparationError ofError);
		
	}
	
	private IOralEvalSDK.ICallback _callback = new IOralEvalSDK.ICallback() {
		
		@Override
		public void onStart(IOralEvalSDK iOralEvalSDK) {
			isFinished = false;
		}
		
		@Override
		public void onError(IOralEvalSDK iOralEvalSDK, IOralEvalSDK.Error error, IOralEvalSDK.OfflineSDKPreparationError ofError) {
			final IOralEvalSDK.Error err = error;
	        final IOralEvalSDK.OfflineSDKPreparationError ofe = ofError;
	        if (null == callback || null == context) {
	        	return;
	        }
	        context.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	if (null != callback) {
	            		callback.onError(err, ofe);
	            	}
	            	context = null;
	            	callback = null;
	            	_oe = null;
//	                showResult("Error:" + err + "\n. offline error:" + ofe);
//	                if(_pd != null) {
//	                    _pd.dismiss();
//	                    _pd = null;
//	                }
	            }
	        });
		}
		
		 @Override
		 public void onStop(IOralEvalSDK iOralEvalSDK, String json, boolean offline, String arg3, int arg4) {
	        DebugPrinter.i("onStop(), offline=" + offline);
	        if (null == callback || null == context) {
	        	return;
	        }
	        //声音回放
//		        if(_record != null){
//		            try {
//		                _record.close();
//		            } catch (IOException e) {
//		                e.printStackTrace();
//		            }
//		            _record = null;
//		        }
	        boolean pass1 = false;
	        
	        int baseScore = GlobalRes.getInstance().getBeans().getSpeechAccuracy();
	        int singleScore = baseScore / 10;
	        double allScore = 0;
	        
	        String htmlStr1 = content;
	        String word = null;
	        int wordIndex = 0;
	        String frontWord = null;
	        String lastWord = null;
	        String replaceWord = null;
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("lines");
				JSONObject jsonObject2 = null;
				if (jsonArray != null) {
					if (jsonArray.length() > 0) {
						jsonObject = jsonArray.getJSONObject(0);
						allScore = jsonObject.getDouble("score");
						jsonArray = jsonObject.getJSONArray("words");
						for (int i = 0; i < jsonArray.length(); ++i) {
							jsonObject2 = jsonArray.getJSONObject(i);
							word = jsonObject2.getString("text");
							int wordIndex1 = htmlStr1.indexOf(word, wordIndex);
							if (wordIndex1 == -1) {
								continue;
							}
							wordIndex = wordIndex1;
							if (singleScore > jsonObject2.getDouble("score")) {
								frontWord = htmlStr1.substring(0, wordIndex);
								lastWord = htmlStr1.substring(wordIndex);
								replaceWord = "<font color='red'>" + word + "</font>";
								htmlStr1 = frontWord + lastWord.replaceFirst(word, replaceWord);
								wordIndex += replaceWord.length();
							}
							else {
								wordIndex += word.length();
							}
						}
						DebugPrinter.i("onStop(), allScore=" + allScore);
						pass1 = allScore > baseScore;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			final boolean pass = pass1;
			final int finalScore = (int) allScore;
			final String htmlStr = htmlStr1;
	        context.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	if (null != callback) {
	            		callback.onResult(pass, finalScore, htmlStr);
	            	}
	            	context = null;
	    	        callback = null;
	                _oe = null;
//		                showResult(rst);
//		                findViewById(R.id.button).setEnabled(true);
//		                if(_pd != null) {
//		                    _pd.dismiss();
//		                    _pd = null;
//		                }
	            }
	        });
	    }

	    @Override
	    public void onVolume(IOralEvalSDK who, final int value) {
	    	DebugPrinter.i("Volume:" + value);
	        if (isFinished) {
	        	return;
	        }
	        if (null != callback) {
	        	callback.onVoice(value / 10);
	        }
	        if (null == firstVoiceTimer) {
	        	firstVoiceTimer = System.currentTimeMillis();
	        }
	        if (System.currentTimeMillis() - firstVoiceTimer < 500) {
	        	return;
	        }
	        int timeOut = count > 1 ? TIME_OUT2 : TIME_OUT1;
	        if (value > 30) {
	        	lastVoiceTimer = System.currentTimeMillis();
	        	return;
	        }
	        if (null == lastVoiceTimer) {
	        	if (null == noVoiceTimer) {
	        		noVoiceTimer = System.currentTimeMillis();
	        		return;
	        	}
        		if (System.currentTimeMillis() - noVoiceTimer > 10000) {
        			getReusult();
        		}
	        	return;
	        }
        	if (System.currentTimeMillis() - lastVoiceTimer > timeOut) {
        		getReusult();
        	}
//		        context.runOnUiThread(new Runnable() {
//		            @Override
//		            public void run() {
//		                ((ProgressBar)findViewById(R.id.progressBar)).setProgress(value);
//		            }
//		        });
	    }
	
	    @Override
	    public void onAudioData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int offset, int len) {
	    	//DebugPrinter.i("got " + len + " bytes of pcm");
	    	 //声音回放
//		        try {
//		            if (null == _record) {
//		                _record = new FileOutputStream(new File(context.getExternalFilesDir(null), "latest.pcm"));
//		            }
//		            _record.write(bytes, offset, len);
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//		        _pcmBuf.write(bytes, offset, len);
	    }

	};
	
	public void start(String content, Activity context, CallbackIOralEvalSDK callback) {
		stop();
		if (null == _oe) {
//			_pcmBuf = new ByteArrayOutputStream();
			this.context = context;
			this.content = content;
			this.callback = callback;
			this.count = TextUtils.isEmpty(content) ? 1 : content.split("[,!?.]").length;
			OralEvalSDKFactory.StartConfig cfg = new OralEvalSDKFactory.StartConfig(content);
			if (USE_OFFLINE_SDK_IF_FAIL_TO_SERVER) {
				cfg.set_useOfflineWhenFailedToConnectToServer(true);
			}
			_oe = OralEvalSDKFactory.start(context, cfg, _callback);
		}
	}

	public void stop() {
		isFinished = true;
		if (_oe == null) {
			return;
		}
		callback = null;
		context = null;
		_oe.stop();
		_oe = null;
		lastVoiceTimer = null;
		noVoiceTimer = null;
		firstVoiceTimer = null;
//		view.setEnabled(false);
//		_pd = new ProgressDialog(this);
//		_pd.show();
	}
	
	public void getReusult() {
		isFinished = true;
		if (_oe == null) {
			return;
		}
		_oe.stop();
		_oe = null;
		lastVoiceTimer = null;
		noVoiceTimer = null;
		firstVoiceTimer = null;
//		view.setEnabled(false);
//		_pd = new ProgressDialog(this);
//		_pd.show();
	}
	
	public void destory(Context context) {
		if (USE_OFFLINE_SDK_IF_FAIL_TO_SERVER)
            OralEvalSDKFactory.cleanupOfflineSDK(context);
	}
	
	/************************************************************************************************************************/
	/**
	/**  声音回放
	/**
	/************************************************************************************************************************/
	
	public AudioTrack playPcmBuf(final Runnable onFinish){
        if(_pcmBuf == null) {
            onFinish.run();
            return null;
        }
        final byte[] pcm = _pcmBuf.toByteArray();
        final int packLen = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
        final AudioTrack at = new AudioTrack(AudioManager.STREAM_VOICE_CALL
                , 16000
                , AudioFormat.CHANNEL_OUT_MONO
                , AudioFormat.ENCODING_PCM_16BIT
                , packLen, AudioTrack.MODE_STREAM);
        try {
            int waitTimes = 3;
            while (at.getState() != AudioTrack.STATE_INITIALIZED && waitTimes > 0) {
                Thread.sleep(100);
                waitTimes--;
            }
            at.play();
        }catch(Exception e){
            DebugPrinter.e("starting audio tracker", e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int offset = 0;
                do{
                    offset += at.write(pcm, offset, offset + packLen > pcm.length ? pcm.length - offset : packLen);
                }while(offset < pcm.length);
                DebugPrinter.i("play audio pcm length:" + offset);
                onFinish.run();
            }
        }).start();
        return at;
    }
}
