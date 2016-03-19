package com.autonavi.mapart.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.autonavi.mapart.entity.ResponseStatus;

public class GetAmapresult implements ApiRequest {
	private Log log = LogFactory.getLog(getClass());
	static String time = DateFormat.get13Now();

	/**
	 * 查询关键字
	 * 
	 * @param key
	 * @return geo
	 */
	public ResponseStatus select(String key,String... types) {
		String enkey = null;
		ResponseStatus status = null;
		try {
			enkey = URLEncoder.encode(key, "utf-8");
			String urlString = "http://www.amap.com/service/signNew?_"
					+ DateFormat.get13Now();
			status = requestURL(1, urlString);
			if (status.getCode() == 100) {
				urlString = "http://s.amap.com/web/ws/mapapi/poi/info/?version=2.0&channel="
						+ status.getRestring().split(",")[2]
						+ "&sign="
						+ status.getRestring().split(",")[0]
						+ "&ts="
						+ status.getRestring().split(",")[1]
						+ "&from=AMAP&index=true&language=zh_cn&query_type=TQUERY&keywords="
						+ enkey
						+ "&output=jsonp&pagesize=10&pagenum=1&qii=true&addr_poi_merge=true&_="
						+ DateFormat.get13Now();
				status = requestURL(3, urlString);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			status = new ResponseStatus(300, "请求错误，请联系管理员!");
		}
		return status;
	}

	// 请求url;
	public ResponseStatus requestURL(int type, String urlString) {
		URL url;
		try {
			url = new URL(urlString);
			Document doc = null;
			String resultString = null;
			if (type == 1) {
				Map<String, String> map = configureRequestArgus();
				doc = Jsoup.connect(urlString).timeout(10 * 1000)
						.header("Accept", "*/*")
						.header("Referer", "http://www.amap.com/")
						.header("Accept-Encoding", "gzip,deflate,sdch")
						.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
						.header("X-Requested-With", "XMLHttpRequest")
						.cookies(map).userAgent("Mozilla") // 设置 User-Agent
						.ignoreContentType(true).get();
				resultString = doc.text();
				JSONObject object = JSONObject.fromObject(resultString);
				return (object.get("status") == null || "0".equals(object.get("status")))?
						new ResponseStatus(300, "False!"):
							new ResponseStatus(100, object.get("sign") + ","
									+ object.get("ts") + "," + object.get("channel"),"success!");
			} else {
				doc = Jsoup.parse(url, 10 * 1000); // 解析获取Document对象
				resultString = Unicode2GBK(doc.text());
				log.debug("amap抓取的参数信息--------:"+resultString);
				// 建立一个解析器
				JSONObject object = JSONObject.fromObject(resultString
						.substring(resultString.indexOf("(") + 1,
								resultString.lastIndexOf(")")));
				boolean result = Boolean.valueOf((String) object.get("result"));
				log.debug("result======="+result);
				if (result) {
					StringBuffer str = new StringBuffer();
					JSONArray poi_list = null;
					try {
						poi_list = object.getJSONArray("poi_list");
					} catch (Exception e) {
						log.debug("eeeeeeeeee:"+e);
						return new ResponseStatus(100, "", "success!");
					}

					for (int i = 0; i < poi_list.size(); i++) {
						JSONObject poi = JSONObject.fromObject(poi_list.get(i));
						String localid = poi.get("localid").toString();
						localid = executeLocalid(localid);
						String latitude = poi.get("latitude").toString();
						String longitude = poi.get("longitude").toString();
						if (localid!=null && !"".equals(localid)) {
							localid += "_"+poi.get("typecode") + "_" + latitude + "_" + longitude;
						}
						str.append(localid + ",");
					}
					return new ResponseStatus(100, str.substring(0,
							str.lastIndexOf(",")), "success!");
				} else {
					return new ResponseStatus(200, "抱歉，在没有找到相关的地点。");
				}
			}

		} catch (MalformedURLException e) {
			log.error(e);
			return new ResponseStatus(300, "请求错误，请联系管理员!");
		} catch (IOException e) {
			log.error(e);
			return new ResponseStatus(300, "请求错误，请联系管理员!");
		}
	}
	private static Map<String, String> configureRequestArgus(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("__utma",
				"240281747.618236846.1389842217.1398043033.1398046447.67");
		map.put("__utmb", "GA1.2.618236846.1389842217");
		map.put("__utmc", "240281747");
		map.put("__utmv", "240281747.|1=panochange=Yes=1");
		map.put("__utmz",
				"240281747.1397625930.64.21.utmcsr=api.amap.com|utmccn=(referral)|utmcmd=referral|utmcct=/javascript/reference/map");
		map.put("_ga", "GA1.2.618236846.1389842217");
		map.put("BIGipServer", "2952858378.20480.0000");
		map.put("CNZZDATA5371206",
				"cnzz_eid%3D1719301977-1389936707-null%26ntime%3D1398048549%26cnzz_a%3D17%26ltime%3D1398043032389%26rtime%3D27");
		map.put("connect.sess",
				"s%3Aj%3A%7B%7D.DffclZ%2FN%2BAiqU5kXMjqg3VQHapScLmBFjbTUDpqgPVQ");
		map.put("key", "b2992a19dfcee083c3e8647b38db8420");
		map.put("pgv_pvi", "6437408768");
		return map;
		
	}
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
	
	public static String  executeLocalid(String localid) {
//		"localid": "J50F001019_257937",
		// localid= J50F001019;J50F001019_17330;337830
		String[] s = null;
		if (localid.indexOf(";") > -1) {
			s = localid.split(";");
			for (String str : s) {
				if (str.indexOf ("_")> -1) {
					return str;
				}
			}
			return "";
		}else {
			return localid;
		}
		
	}
	

	public static void main(String[] args) {
		System.out.println(new GetAmapresult().select("颐和园"));
	}
}
