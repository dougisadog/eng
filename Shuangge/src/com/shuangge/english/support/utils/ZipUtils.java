package com.shuangge.english.support.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ZipUtils {

	private static final int BUFFER_SIZE = 1024; 
	
	public static Bitmap getBitmapFromZipFile(String zipFilePath, String fileName) {
		ZipFile zip = null;
		Bitmap result = null;
		try {
			zip = new ZipFile(zipFilePath);
			ZipEntry entry = zip.getEntry(fileName);
			InputStream is = zip.getInputStream(entry);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while ((count = is.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
			byte[] bytes = baos.toByteArray();
			result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			Utility.closeSilently(baos);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != zip) {
				Utility.closeSilently(zip);
			}
		}
		return result;
	}

	public static String getTextFromZipFile(String zipFilePath, String fileName) {
		ZipFile zip = null;
		String result = null;
		try {
			zip = new ZipFile(zipFilePath);
//			Enumeration<?> es = zip.entries();
//			while (es.hasMoreElements()) {
//				ZipEntry entry = (ZipEntry) es.nextElement();
//				System.out.println(entry.getName());
//			}
			ZipEntry entry = zip.getEntry(fileName);
			InputStream is = zip.getInputStream(entry);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while ((count = is.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
			byte[] bytes = baos.toByteArray();
			result = new String(bytes, "UTF-8");
			Utility.closeSilently(baos);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != zip) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean getListFromZipFile(String zipFilePath) {
		ZipFile zip = null;
		boolean result = false;
		try {
			zip = new ZipFile(zipFilePath);
			Enumeration<?> es = zip.entries();
			while (es.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) es.nextElement();
				System.out.println(entry.getName()); 
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != zip) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean uncompression(String zipFilePath, String targetDir) {
		ZipFile zip = null;
		boolean result = false;
		try {
			File file = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			InputStream is = null;
			zip = new ZipFile(zipFilePath);
			Enumeration<?> es = zip.entries();
			while (es.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) es.nextElement();
				is = zip.getInputStream(entry);
				file = new File(targetDir + entry.getName());
				File dir = file.getParentFile();
				if (!dir.exists()) {
					dir.mkdir();
				}
				fos = new FileOutputStream(file);
				int count = 0;
				byte[] buffer = new byte[BUFFER_SIZE];
				bos = new BufferedOutputStream(fos, BUFFER_SIZE);  
    			while ((count = is.read(buffer)) != -1) {
    				bos.write(buffer, 0, count);
    			}
    			bos.flush();
    			Utility.closeSilently(bos);
            	Utility.closeSilently(fos);
            	Utility.closeSilently(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		finally {
			if (null != zip) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
}
