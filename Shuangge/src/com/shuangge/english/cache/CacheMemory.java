package com.shuangge.english.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.util.LruCache;

public class CacheMemory {

	public static final int SOFT_CACHE_SIZE = 0;
	
	// LinkedHashMap初始容量
	private static final int INITIAL_CAPACITY = 16;
	
	// LinkedHashMap加载因子
	private static final float LOAD_FACTOR = 0.75f;
	
	// LinkedHashMap排序模式
	private static final boolean ACCESS_ORDER = true;

	// 硬引用缓存
	private static MyLruCache<Bitmap> cacheBitmap;

	public CacheMemory() {
		// 获取单个进程可用内存的最大值
		// 方式一：使用ActivityManager服务（计量单位为M）
		/*
		 * int memClass = ((ActivityManager)
		 * context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		 */
		// 方式二：使用Runtime类（计量单位为Byte）
		final int memClass = (int) Runtime.getRuntime().maxMemory();
		// 设置为可用内存的1/4（按Byte计算）
		final int cacheSize = memClass / 4;
		cacheBitmap = new MyLruCache<Bitmap>(cacheSize);
	}

	/**
	 * 从缓存中获取Bitmap
	 * 
	 * @param url
	 * @return bitmap
	 */
	public Bitmap getBitmapFromMem(String url, int w, int h) {
		Bitmap bitmap = null;
		if (w != 0 && h != 0) {
			url = url + "_" + w + "_" + h;
		}
		// 先从硬引用缓存中获取
		synchronized (cacheBitmap) {
			bitmap = cacheBitmap.get(url);
			if (bitmap != null) {
				// 找到该Bitmap之后，将其移到LinkedHashMap的最前面，保证它在LRU算法中将被最后删除。
				cacheBitmap.remove(url);
				cacheBitmap.put(url, bitmap);
				return bitmap;
			}
		}

		// 再从软引用缓存中获取
		synchronized (cacheBitmap.getSoftCache()) {
			SoftReference<Bitmap> bitmapReference = cacheBitmap.getSoftCache().get(url);
			if (bitmapReference != null) {
				bitmap = bitmapReference.get();
				if (bitmap != null) {
					// 找到该Bitmap之后，将它移到硬引用缓存。并从软引用缓存中删除。
					cacheBitmap.put(url, bitmap);
					cacheBitmap.getSoftCache().remove(url);
					return bitmap;
				} 
				else {
					cacheBitmap.getSoftCache().remove(url);
				}
			}
		}
		return null;
	}

	/**
	 * 添加Bitmap到内存缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void addBitmapToCache(String url, int w, int h, Bitmap bitmap) {
		if (bitmap != null) {
			if (w != 0 && h != 0) {
				url = url + "_" + w + "_" + h;
			}
			synchronized (cacheBitmap) {
				cacheBitmap.put(url, bitmap);
			}
		}
	}

	/**
	 * 清理软引用缓存
	 */
	public void clearCache() {
		cacheBitmap.clearCache();
	}
	
	private class MyLruCache<T> extends LruCache<String, T> {
		
		// 软引用缓存
		private LinkedHashMap<String, SoftReference<T>> softCache;
		
		public MyLruCache(int maxSize) {
			super(maxSize);
			/*
			 * 第一个参数：初始容量（默认16） 第二个参数：加载因子（默认0.75）
			 * 第三个参数：排序模式（true：按访问次数排序；false：按插入顺序排序）
			 */
			initSoftCache();
		}
		
		@Override
		protected int sizeOf(String key, T value) {
			if (value != null) {
				if (value instanceof Bitmap) {
					// 计算存储bitmap所占用的字节数
					return ((Bitmap) value).getRowBytes() * ((Bitmap) value).getHeight();
				}
				if (value instanceof MediaPlayer) {
					// 计算存储bitmap所占用的字节数
					return ((MediaPlayer) value).getVideoWidth() * ((MediaPlayer) value).getVideoWidth();
				}
			} 
			return 0;
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				T oldValue, T newValue) {
			if (oldValue != null) {
				// 当硬引用缓存容量已满时，会使用LRU算法将最近没有被使用的图片转入软引用缓存
				softCache.put(key, new SoftReference<T>(oldValue));
			}
		}
		
		public void initSoftCache() {
			softCache = new LinkedHashMap<String, SoftReference<T>>(INITIAL_CAPACITY, LOAD_FACTOR, ACCESS_ORDER) {
				private static final long serialVersionUID = 7237325113220820312L;

				@Override
				protected boolean removeEldestEntry(Entry<String, SoftReference<T>> eldest) {
					if (size() > SOFT_CACHE_SIZE) {
						return true;
					}
					return false;
				}
			};
		}
		
		/**
		 * 清理软引用缓存
		 */
		public void clearCache() {
			softCache.clear();
			softCache = null;
		}

		public LinkedHashMap<String, SoftReference<T>> getSoftCache() {
			return softCache;
		}
		
	}
	
}