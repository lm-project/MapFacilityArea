package com.autonavi.mapart.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.autonavi.mapart.entity.ResponseStatus;

public class GetQQMapresult implements ApiRequest {

	private Log log = LogFactory.getLog(getClass());
	public static int m = 0;
	/**
	 * 查询关键字所在设施区域
	 * 
	 * @param key
	 * @return
	 */
	public ResponseStatus select(String key,String... types) {
		ResponseStatus response = null;
		String enkey = CommonUtil.encode(key);
		if (enkey != null) {
			response = request(enkey);
			// 如果查询无结果且查询关键字中包含特殊字符，如：（）()等， 则去除留中文继续再次搜索
			if (response.getCode() == 100
					&& ("".equals(response.getRestring()) || response
							.getRestring() == null)
					&& (key.indexOf("(") > -1 || key.indexOf("（") > -1)) {

				key = key.substring(0, key.length() - 1);
				String[] keys = key.indexOf("(") > -1 ? key.split("\\(") : key
						.split("\\（");
				enkey = CommonUtil.encode(keys[0] + keys[1]);
				response = request(enkey);
			}
		} else {
			response = new ResponseStatus(200, "error", "查询参数错误！");
		}
		return response;
	}

	/**
	 * 查询关键字所在设施区域
	 * 
	 * @param key
	 * @return geo
	 */
	private ResponseStatus request(String enkey) {
		String urlString = "http://route.map.qq.com/?qt=poi&c=3&wd="
				+ enkey
				+ "&pn=0&rn=9&bl=1&nj=0&nr=0&nf=1&l=15&"
				+ "rl=9&lc=1&nqc=0&owd=&qct=&sid=cdf9df31415e095fa6ac57044004baa9&output=jsonp&cb=QQMapLoader.cbhudd5mwo8";
		ResponseStatus status = requestURL(1, urlString);
		return status;
	}

	// 请求url;
	public ResponseStatus requestURL(int type, String urlString) {
		@SuppressWarnings("unused")
		URL url = null;
		Document doc = null;
		ResponseStatus responseSta = null;
		try {
			
			url = new URL(urlString);
			doc = Jsoup.connect(urlString).timeout(110 * 1000)
					.header("Accept", "*/*")
					.header("Referer", "http://map.qq.com/")
					.header("Accept-Encoding", "gzip,deflate,sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
					.header("X-Requested-With", "XMLHttpRequest")
					.ignoreContentType(true).get();
			// 建立一个解析器
			String resultString = CommonUtil.Unicode2GBK(doc.text());
			if(!resultString.endsWith(")")){
				resultString=resultString+")";
			}
			//log.debug(resultString);
			String result = executeDom(resultString.substring(
					resultString.indexOf("(") + 1, resultString.lastIndexOf(")")));
		
			responseSta = new ResponseStatus(100, result==null ?"" : result , "success");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			responseSta = new ResponseStatus(300, "false", "访问被拒绝!");
		} 
		return responseSta;
	}

	// 解析DOM元素
	private String executeDom(String dom) {
		try {
			JSONObject object = JSONObject.fromObject(dom);
			JSONObject detail = object.getJSONObject("detail");
			JSONArray pois = null;
			pois = detail.getJSONArray("pois");
			if (pois != null) {
				JSONObject poi = JSONObject.fromObject(pois.get(0));
				JSONObject richInfo = poi.getJSONObject("richInfo");
				if (richInfo != null) {
					String area = richInfo.getString("area");
					if (!"".equals(area)) {
						return area.replaceAll(";", ",");
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
			m++;
			return null;
		}
		return null;
	}

	/**
	 * for test
	 * 
	 * @author qiang.cai
	 * 
	 */
	public class Test implements Runnable {
		String key;
		int threadNum;

		public Test() {
		}

		public Test(String key, int threadNum) {
			this.key = key;
			this.threadNum = threadNum;

		}

		public void run() {
			System.out.println(select(key));
//			select(key);
		}
	}
	
	
	public static void main(String[] args) {
		Date start = new Date();
		GetQQMapresult result = new GetQQMapresult();
		GetQQMapresult.Test test = result.new Test("乌鲁木齐市银都度假村", 1);
		for (int i = 0; i < 1; i++) {
			new Thread(test).run();;
		}
		
//		System.out.println(result.select("北京大学"));
		Date end = new Date();
		System.out.println("执行错误数："+ m + "," + (end.getTime() - start.getTime()) / 1000.0 + " 秒");
	}
}
