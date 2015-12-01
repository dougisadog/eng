package com.shuangge.english.support.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MathUtils {

	public static String[] ramdomStringArray(String[] arr) {
		String[] arr1 = new String[arr.length];
		for (int i = 0; i < arr.length; ++i) {
			arr1[i] = arr[i];
		}
		String[] arr2 = new String[arr1.length];
		int count = arr1.length;
		int cbRandCount = 0;// 索引
		int cbPosition = 0;// 位置
		int k = 0;
		do {
			Random rand = new Random();
			int r = count - cbRandCount;
			cbPosition = rand.nextInt(r);
			arr2[k++] = arr1[cbPosition];
			cbRandCount++;
			arr1[cbPosition] = arr1[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition
		} while (cbRandCount < count);
		return arr2;
	}

	public static void ramdomList(List list) {
		Collections.sort(list, new Comparator<Object>() {
			HashMap map = new HashMap();

			public int compare(Object v1, Object v2) {
				init(v1);
				init(v2);

				double n1 = ((Double) map.get(v1)).doubleValue();
				double n2 = ((Double) map.get(v2)).doubleValue();
				if (n1 > n2)
					return 1;
				else if (n1 < n2)
					return -1;
				return 0;
			}

			private void init(Object v) {
				if (map.get(v) == null) {
					map.put(v, new Double(Math.random()));
				}
			}

			protected void finalize() throws Throwable {
				map = null;
			}
		});
	}

}
