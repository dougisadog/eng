package com.shuangge.english.support.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import air.com.shuangge.phone.ShuangEnglish.BuildConfig;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory.ReqFile;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.utils.Utility;

/**
 * 
 * @author Jeffrey
 *
 */
public class HttpProcess {

    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int READ_TIMEOUT = 10 * 1000;
    private static final int DOWNLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int DOWNLOAD_READ_TIMEOUT = 60 * 1000;
    private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;
    
    private static final int MAX_RECONNECTION_NUM = 2;
    private int reconnectionNum = 0;
    
    public static String SESSION_ID = null;  

    @SuppressLint("TrulyRandom")
	public HttpProcess() {
        //allow Android to use an untrusted certificate for SSL/HTTPS connection
        //so that when you debug app, you can use Fiddler http://fiddler2.com to logs all HTTPS traffic
        try {
            if (BuildConfig.DEBUG) {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
					
				});
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[] {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                }, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            }
        } 
        catch (Exception e) {
        }

    }

    /**
     * 获取代理配置
     * @return
     */
    public static Proxy getProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort)) {
            return new Proxy(java.net.Proxy.Type.HTTP,
                    new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
        } else {
            return null;
        }
    }
    
    public boolean doGet(String urlStr, final HttpReqFactory.ReqParam[] params, final HttpReqHelper.HttpReqListener listener) {
        try {
        	URL url = new URL(urlStr + "?" + Utility.convertParamsToUrl(params));
            DebugPrinter.d("get request" + url);
            Proxy proxy = getProxy();
            HttpURLConnection huc = null;
            if (proxy != null) {
                huc = (HttpURLConnection) url.openConnection(proxy);
            } 
            else {
                huc = (HttpURLConnection) url.openConnection();
            }
            huc.setRequestMethod("GET");
            huc.setDoOutput(false);
            huc.setConnectTimeout(CONNECT_TIMEOUT);
            huc.setReadTimeout(READ_TIMEOUT);
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Charset", "UTF-8");
            huc.setRequestProperty("Accept-Encoding", "gzip, deflate");
            huc.connect();
            return handleResponse(huc, listener);
        } 
        catch (IOException e) {
        	if (++reconnectionNum < MAX_RECONNECTION_NUM) {
        		return doGet(urlStr, params, listener);
        	}
            if (null != listener) {
            	listener.errorHandler(e, GlobalApp.getInstance().getResources().getString(R.string.timeout));
            }
        }
        return false;
    }

    public boolean doPost(String urlStr, final HttpReqFactory.ReqParam[] params, final HttpReqHelper.HttpReqListener listener) {
        try {
            URL url = new URL(urlStr);
            Proxy proxy = getProxy();
            HttpURLConnection huc = null;
            if (proxy != null) {
                huc = (HttpURLConnection) url.openConnection(proxy);
            } 
            else {
                huc = (HttpURLConnection) url.openConnection();
            }
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setRequestMethod("POST");
            huc.setUseCaches(false);
            huc.setConnectTimeout(CONNECT_TIMEOUT);
            huc.setReadTimeout(READ_TIMEOUT);
            huc.setInstanceFollowRedirects(false);
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Charset", "UTF-8");
            huc.setRequestProperty("Accept-Encoding", "gzip, deflate");
            huc.connect();
            DataOutputStream out = new DataOutputStream(huc.getOutputStream());
            out.write(Utility.convertParamsToUrl(params).getBytes());
            Utility.closeSilently(out);
            return handleResponse(huc, listener);
        } 
        catch (IOException e) {
        	if (++reconnectionNum < MAX_RECONNECTION_NUM) {
        		return doPost(urlStr, params, listener);
        	}
            if (null != listener) {
            	listener.errorHandler(e, GlobalApp.getInstance().getResources().getString(R.string.timeout));
            }
        }
        return false;
    }

    private boolean handleResponse(HttpURLConnection httpURLConnection, HttpReqHelper.HttpReqListener listener) {
        int status = 0;
        try {
            status = httpURLConnection.getResponseCode();
            if (null != listener)
            	listener.completeHandler(readResult(httpURLConnection));
        } 
        catch (Exception e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
            if (null != listener)
            	listener.errorHandler(e, null);
            return false;
        }
        if (status != HttpURLConnection.HTTP_OK) {
        	if (null != listener) 
        		listener.errorHandler(handleError(httpURLConnection), null);
        	return false;
        }
        return true;
    }

    private Exception handleError(HttpURLConnection huc) {
    	return readError(huc);
    }

    private String readResult(HttpURLConnection huc) throws Exception {
        InputStream is = null;
        BufferedReader buffer = null;
        try {
            is = huc.getInputStream();
            String contentEncoding = huc.getContentEncoding();
            if (!TextUtils.isEmpty(contentEncoding) && contentEncoding.equals("gzip")) {
                is = new GZIPInputStream(is);
            }
            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
//            DebugPrinter.d("result=" + strBuilder.toString());
            return strBuilder.toString();
        } 
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception(GlobalApp.getInstance().getResources().getString(R.string.timeout), e);
        } 
        finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            huc.disconnect();
        }

    }

    private Exception readError(HttpURLConnection huc) {
    	Exception e = null;
        InputStream is = null;
        BufferedReader buffer = null;
        try {
            is = huc.getErrorStream();
            if (is == null) {
                e = new Exception(GlobalApp.getInstance().getResources().getString(R.string.timeout));
            }
            else {
            	String contentEncoding = huc.getContentEncoding();
            	if (!TextUtils.isEmpty(contentEncoding) && contentEncoding.equals("gzip")) {
            		is = new GZIPInputStream(is);
            	}
            	buffer = new BufferedReader(new InputStreamReader(is));
            	StringBuilder strBuilder = new StringBuilder();
            	String line;
            	while ((line = buffer.readLine()) != null) {
            		strBuilder.append(line);
            	}
            	DebugPrinter.d("error result=" + strBuilder.toString());
            	e = new Exception(strBuilder.toString());
            }
        } 
        catch (IOException e1) {
            e.printStackTrace();
            e = new Exception(GlobalApp.getInstance().getResources().getString(R.string.timeout), e1);
        } 
        finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            huc.disconnect();
        }
        return e;

    }

	public boolean doGetAndSaveFile(String urlStr, File file, DownloadState state, final HttpReqHelper.DownloadFileListener listener) {
        boolean result = true;
        FileOutputStream fileOut = null;
        InputStream in = null;
        HttpURLConnection huc = null;
        try {
            URL url = new URL(urlStr);
            DebugPrinter.d("download request=" + urlStr);
            Proxy proxy = getProxy();
            if (proxy != null) {
                huc = (HttpURLConnection) url.openConnection(proxy);
            } else {
                huc = (HttpURLConnection) url.openConnection();
            }
            huc.setRequestMethod("GET");
            huc.setDoOutput(false);
            huc.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
            huc.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Charset", "UTF-8");
            huc.setRequestProperty("Accept-Encoding", "gzip, deflate");
            long range = state.isEncrypted && state.range > 0 ? state.range + DownloadState.ENCRYPTED_HEAD_LEN : state.range;
            if (range > 0) {
            	huc.setRequestProperty("RANGE", "bytes=" + range + "-");
            }
            huc.connect();
            int status = huc.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_PARTIAL) {
            	//range 错误从新下载
            	if (status == 416) {
            		if (file.exists()) {
                    	file.delete();
                    	file.createNewFile();
            		}
            		state.range = 0;
                	return doGetAndSaveFile(urlStr, file, state, listener);
            	}
            	result = false;
            	listener.errorHandler(new Exception("is not 200 is: " + status), GlobalApp.getInstance().getResources().getString(R.string.timeout));
                return result;
            }
            long bytetotal = (long) huc.getContentLength();
            long bytesum = 0;
        	bytesum = state.range;
        	bytetotal += state.range; 
            int byteread = 0;
            if (!file.exists()) {
            	file.createNewFile();
            }
            fileOut = new FileOutputStream(file, true);
            InputStream is = huc.getInputStream();
            String content_encode = huc.getContentEncoding();
            if (!TextUtils.isEmpty(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }
            in = new BufferedInputStream(is);
            if (state.isEncrypted && state.range == 0) {
            	int headLen = DownloadState.ENCRYPTED_HEAD_LEN;
            	while(headLen > 0) {
            		in.read();
            		headLen--;
            	}
            	bytetotal -= DownloadState.ENCRYPTED_HEAD_LEN;
            }
            final Thread thread = Thread.currentThread();
            byte[] buffer = new byte[1444];
            if (null != listener) {
            	listener.startHandler(bytetotal);
            }
            while ((byteread = in.read(buffer)) != -1) {
                if (thread.isInterrupted()) {
                    if (((float) bytesum / (float) bytetotal) < 0.8f) {
                        file.delete();
                        throw new InterruptedIOException();
                    }
                }
                bytesum += byteread;
                fileOut.write(buffer, 0, byteread);
                if (null != listener && bytetotal > 0) {
                	listener.progressHandler(bytesum, bytetotal);
                }
                if (!state.allowedTOdownload) {
                    result = false;
                    break;
                }
            }
            fileOut.flush();
            if (null != listener) {
            	if (result) {
            		listener.completeHandler(file);
            	}
            	else {
            		listener.stopHandler(bytesum, bytetotal);
            	}
            }
            DebugPrinter.v("download request= " + urlStr + " download finished");
        } 
        catch (IOException e) {
            if (file.exists()) {
            	file.delete();
            }
            if (++reconnectionNum < MAX_RECONNECTION_NUM) {
            	state.range = 0;
        		return doGetAndSaveFile(urlStr, file, state, listener);
        	}
            DebugPrinter.v("download request= " + urlStr + " download failed");
            if (null != listener)
            	listener.errorHandler(e, GlobalApp.getInstance().getResources().getString(R.string.timeout));
            result = false;
        } 
        finally {
            Utility.closeSilently(in);
            Utility.closeSilently(fileOut);
            if (huc != null) {
                huc.disconnect();
            }
        }
        return result;
    }
	
	public static class DownloadState {
		
		public static final int ENCRYPTED_HEAD_LEN = 16;
		
		private Object param;
		private boolean isEncrypted = false;
		private long range = 0;
		private boolean allowedTOdownload = true;
		private String filePath;
		
		private CallbackDownloadState callback;
		
		public void startInBackground(long fileLength) {
			if (null != callback)
				callback.startInBackground(param, fileLength);
		}
		
		public void finishInBackground() {
			if (null != callback)
				callback.finishInBackground(param);
		}
		
		public void onError(Exception e, String errorMsg) {
			if (null != callback)
				callback.onError(e, errorMsg);
		}
		
		public DownloadState() {
		}
		
		public DownloadState(boolean isEncrypted, long range) {
			this.isEncrypted = isEncrypted;
			this.range = range;
		}
		
		public DownloadState(boolean isEncrypted, long range, CallbackDownloadState callback, Object param) {
			this.isEncrypted = isEncrypted;
			this.range = range;
			this.callback = callback;
			this.param = param;
		}

		public boolean isAllowedTOdownload() {
			return allowedTOdownload;
		}

		public void setAllowedTOdownload(boolean allowedTOdownload) {
			this.allowedTOdownload = allowedTOdownload;
		}

		public long getRange() {
			return range;
		}

		public void setRange(long range) {
			this.range = range;
		}

		public boolean isEncrypted() {
			return isEncrypted;
		}

		public void setEncrypted(boolean isEncrypted) {
			this.isEncrypted = isEncrypted;
		}

		public Object getParam() {
			return param;
		}

		public void setParam(Object param) {
			this.param = param;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

	}
	
	public static interface CallbackDownloadState {
		
		public void startInBackground(Object param, long fileLength);
		
		public void finishInBackground(Object param);
		
		public void onError(Exception e, String errorMsg);
		
	}
    
    
	public boolean doGetFile(String urlStr, final HttpReqHelper.DownloadBinaryFileListener listener) {
        boolean result = false;
        ByteArrayOutputStream byteArryOut = null;
        InputStream in = null;
        HttpURLConnection huc = null;
        try {
            URL url = new URL(urlStr);
            DebugPrinter.d("download request=" + urlStr);
            Proxy proxy = getProxy();
            if (proxy != null) {
                huc = (HttpURLConnection) url.openConnection(proxy);
            } else {
                huc = (HttpURLConnection) url.openConnection();
            }
            huc.setRequestMethod("GET");
            huc.setDoOutput(false);
            huc.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
            huc.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Charset", "UTF-8");
            huc.setRequestProperty("Accept-Encoding", "gzip, deflate");
            huc.connect();
            int status = huc.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_PARTIAL) {
            	result = false;
            	listener.errorHandler(new Exception("is not 200"), GlobalApp.getInstance().getResources().getString(R.string.timeout));
                return result;
            }
            long bytetotal = (int) huc.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            File file = FileUtils.createNewTempFileByUrl(urlStr);
            byteArryOut = new ByteArrayOutputStream();
            InputStream is = huc.getInputStream();
            String content_encode = huc.getContentEncoding();
            if (!TextUtils.isEmpty(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }
            in = new BufferedInputStream(is);
            final Thread thread = Thread.currentThread();
            byte[] buffer = new byte[1444];
            while ((byteread = in.read(buffer)) != -1) {
                if (thread.isInterrupted()) {
                    if (((float) bytesum / (float) bytetotal) < 0.8f) {
                        file.delete();
                        throw new InterruptedIOException();
                    }
                }
                bytesum += byteread;
                byteArryOut.write(buffer, 0, byteread);
                if (null != listener && bytetotal > 0) {
                	listener.progressHandler((int) bytesum, (int) bytetotal);
                }
            }
            if (null != listener) {
            	listener.completeHandler(byteArryOut.toByteArray());
            }
            DebugPrinter.v("download request= " + urlStr + " download finished");
            result = true;
        } 
        catch (IOException e) {
        	if (++reconnectionNum < MAX_RECONNECTION_NUM) {
        		return doGetFile(urlStr, listener);
        	}
            DebugPrinter.v("download request= " + urlStr + " download failed");
            if (null != listener) {
            	listener.errorHandler(e, GlobalApp.getInstance().getResources().getString(R.string.timeout));
            }
            result = false;
        } 
        finally {
            Utility.closeSilently(in);
            Utility.closeSilently(byteArryOut);
            if (huc != null) {
                huc.disconnect();
            }
        }
        return result;
    }

    private static String getBoundry() {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                _sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                _sb.append((char) (65 + time % 26));
            } else {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }

	public boolean doUploadFile(String urlStr, final HttpReqFactory.ReqFile[] files, final HttpReqFactory.ReqParam[] params, final HttpReqHelper.UploadFileFileListener listener) {
    	boolean result = false;
        String BOUNDARYSTR = getBoundry();
        HttpURLConnection huc = null;
        DataOutputStream out = null;
        FileInputStream fis = null;
        try {
            URL url = new URL(urlStr);
            Proxy proxy = getProxy();
            if (proxy != null) {
                huc = (HttpURLConnection) url.openConnection(proxy);
            } 
            else {
                huc = (HttpURLConnection) url.openConnection();
            }
            huc.setConnectTimeout(UPLOAD_CONNECT_TIMEOUT);
            huc.setReadTimeout(UPLOAD_READ_TIMEOUT);
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setRequestMethod("POST");
            huc.setUseCaches(false);
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Charset", "UTF-8");
            huc.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            huc.connect();
            out = new DataOutputStream(huc.getOutputStream());
            ReqParam param = null;
            for (int i = 0; i < params.length; i++) {
            	param = params[i];
            	out.writeBytes("--" + BOUNDARYSTR + "\r\n");  
            	out.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey()  
                        + "\"\r\n");  
            	out.writeBytes("\r\n");
            	out.write(param.getValue().toString().getBytes("UTF-8"));
            	out.writeBytes("\r\n");  
			}
            ReqFile fileParam = null;
            for (int i = 0; i < files.length; i++) {
            	fileParam = files[i];
            	out.writeBytes("--" + BOUNDARYSTR + "\r\n");  
            	out.writeBytes("Content-Disposition: form-data; name=\"" + fileParam.getFeildName()
                        + "\"; filename=\"" + URLEncoder.encode(fileParam.getFile().getName(), "UTF-8") + "\"\r\n");
            	out.writeBytes("Content-Type: " + fileParam.getFileType() + "\r\n");  
            	out.writeBytes("\r\n");  
            	out.write(getBytes(fileParam.getFile()));  
            	out.writeBytes("\r\n");  
            }
            out.writeBytes("--" + BOUNDARYSTR + "--" + "\r\n");  
            out.writeBytes("\r\n");
            out.flush();
            Utility.closeSilently(out);
            if (listener != null) {
                listener.waitServerResponse();
            }
            int status = huc.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
            	if (null != listener) {
            		listener.errorHandler(handleError(huc), null);
            	}
            }
            else {
            	 handleResponse(huc, listener);
            }
        	//删除文件？？？
            result = true;
        } 
        catch (Exception e) {
        	if (++reconnectionNum < MAX_RECONNECTION_NUM) {
         		return doUploadFile(urlStr, files, params, listener);
         	}
            if (null != listener) {
            	listener.errorHandler(e, GlobalApp.getInstance().getResources().getString(R.string.timeout));
            }
        } 
        finally {
            Utility.closeSilently(fis);
            Utility.closeSilently(out);
            if (huc != null) {
                huc.disconnect();
            }
        }
        return result;
    }
    
    private byte[] getBytes(File f) throws Exception {  
        FileInputStream in = new FileInputStream(f);  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        byte[] b = new byte[1024];  
        int n;  
        while ((n = in.read(b)) != -1) {  
            out.write(b, 0, n);  
        }  
        in.close();  
        return out.toByteArray();  
    } 
    
}