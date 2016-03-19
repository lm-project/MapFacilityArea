package com.autonavi.mapart.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CommonUtil {
	
	
	/**
	 * GBK 转 Unicode
	 * 
	 * @param key
	 * @return
	 */
	public static String encode(String key) {
		try {
			return URLEncoder.encode(key, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Unicode 转 GBK
	 * 
	 * @param dataStr
	 * @return
	 */
	public static String Unicode2GBK(String dataStr) {
		int index = 0;
		StringBuffer buffer = new StringBuffer();

		int li_len = dataStr.length();
		while (index < li_len) {
			if (index >= li_len - 1
					|| !"\\u".equals(dataStr.substring(index, index + 2))) {
				buffer.append(dataStr.charAt(index));
				index++;
				continue;
			}

			String charStr = "";
			charStr = dataStr.substring(index + 2, index + 6);
			char letter = (char) Integer.parseInt(charStr, 16);
			buffer.append(letter);
			index += 6;
		}
		return buffer.toString();
	}
}