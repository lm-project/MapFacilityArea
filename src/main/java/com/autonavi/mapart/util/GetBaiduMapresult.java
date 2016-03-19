package com.autonavi.mapart.util;

import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.autonavi.mapart.entity.ResponseStatus;

public class GetBaiduMapresult implements ApiRequest {
	private static final String SEARCH_POI_URL = "http://api.map.baidu.com/place/v2/search?ak=A9AGCgtSHywDDpcy1qM4vhGC"
			+ "&output=json&query=#SEARCH_KEY#&page_size=20"
			+ "&page_num=#PAGE_NUM#&scope=2&region=#REGION#";
	
	private Log log = LogFactory.getLog("myLog");
	private enum Type {
		polygon,poi
	}
	private final String SEARCH_POLYON_URL = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&qt=ext&uid="
			+ "#UID#"
			+ "&c="
			+ "#SEARCH_KEY#"
			+ "&ext_ver=new&tn=B_NORMAL_MAP&nn=0&ie=utf-8&l=16";
	
	
	StringBuffer sb = new StringBuffer();
	/**
	 * 查询关键字所在设施区域
	 * 
	 * @param key
	 * @return
	 */
	public ResponseStatus select(String key,String... types) {
		ResponseStatus response = null;
		Type type = types == null || types.length == 0 ? Type.polygon : Type.poi;
		String enkey = CommonUtil.encode(key);
		if (enkey != null) {
			response = request(enkey, type ,types);
			// 如果查询无结果且查询关键字中包含特殊字符，如：（）()等， 则去除留中文继续再次搜索
			if (response.getCode() == 100 && 
					("".equals(response.getRestring()) || response.getRestring() == null)
					&& (key.indexOf("(") > -1 || key.indexOf("（") > -1)) {

				key = key.substring(0, key.length() - 1);
				String[] keys = key.indexOf("(") > -1 ? key.split("\\(") : key.split("\\（");
				enkey = CommonUtil.encode(keys[0] + keys[1]);
				response = request(enkey, type ,types);
			}
			if (StringUtils.isNotBlank(response.getRestring())&&response.getCode() == 100&&response!=null) {
				response = new ResponseStatus(100, GetGeometry.getBaiduLngLat(response.getRestring()), 
						response.getDescription());
				log.debug("---------------百度坐标转换--------");
				log.debug(response.getRestring());
			}
		} else {
			response = new ResponseStatus(200, "error", "查询参数错误！");
		}
		return response;
	}
	
	private ResponseStatus request(String enkey, Type type, String... types) {
		ResponseStatus status = null;
		// 组织URL 串
		String urlString = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap"
				+ "&qt=s&da_src=pcmappg.searchBox.button&wd="
				+ enkey
				+ "&src=0&wd2=&sug=0&"
				+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t="
				+ DateFormat.get13Now();
		// 解析返回html页面元素 code : 100 - 请求正常 ； 200 - 请求异常 ； 300 -目标服务器拒绝访问
		status = processPolygonURL(1, urlString);
		if (status.getCode() == 100) {
			//FIXME 数组越界
			String result = status.getRestring();
			String[] arrays = result.split(",");
			if( arrays.length < 2) {
				return status;
			}
			if( type == Type.polygon) {
				urlString = SEARCH_POLYON_URL.replaceAll("#UID#", arrays[0]).replaceAll("#SEARCH_KEY#", arrays[1]);
				status = processPolygonURL(2, urlString);
			} else {
				urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", enkey).replaceAll("#PAGE_NUM#", types[1]).replaceAll("#REGION#", types[0]);
					return new ResponseStatus(100, result==null ? "" : getPoiDataLength(enkey,types,urlString) , "success");
			}
			
		}
		return status;
	}

	// 模拟浏览器请求url
	public ResponseStatus processPolygonURL(int type, String urlString) {
		try {
			Document doc = requestURL(urlString);
			String result = executeDom(type, CommonUtil.Unicode2GBK(doc.text()));
			return new ResponseStatus(100, result==null ? "" : result , "success");
		} catch (Exception e) {
			return new ResponseStatus(300, "error", "访问被拒绝!"+e.getMessage());
		}
		// 返回解析值
	}

	private Document requestURL(String urlString) throws Exception {
		try {
			return Jsoup.parse(new URL(urlString), 10 * 1000);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void processPoi(String url, int pageIndex) {
		
		 try {
			Document doc = requestURL(url);
			log.debug("request url:" + url);
			log.debug("第"+pageIndex+"页：" + CommonUtil.Unicode2GBK(doc.text()));
			JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
			JSONArray array = json.getJSONArray("results");
			for(int i=0;i< array.size() ; i ++) {
				JSONObject poi = array.getJSONObject(i);
				try {
					log.debug(i+ ": "+poi.getJSONObject("detail_info").toString());
					if( StringUtils.contains(poi.getJSONObject("detail_info").getString("tag"),"住宅区")){
						sb.append(poi.getString("name")).append(",");
					}
				} catch(Exception e) {
					log.error( poi.toString() + "," + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getPoiDataLength(String enkey, String[] types, String url){
		try {
			Document doc = requestURL(url);
			JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
			final int total = Integer.parseInt(json.getString("total"));
			final int totalPage = total/20==0 ? total/20 : total/20+1;
			final int totalPages = totalPage==0 ? 1 : totalPage;
			log.info("总数："+total+"页数："+totalPage+"   从第"+types[1]+"页开始抓取");
			if(total<=0){return "";}
			for( int pageIndex = Integer.parseInt(types[1]); pageIndex < totalPages; pageIndex++ ){
				String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", enkey).replaceAll("#PAGE_NUM#", String.valueOf(pageIndex)).replaceAll("#REGION#", types[0]);
				processPoi(urlString,pageIndex);
				/*if(pageIndex % 10 == 0 ) {
					Thread.sleep( 10 * 1000 );
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 解析DOM元素
	private String executeDom(int type, String dom) {
		try{
			JSONObject object = JSONObject.fromObject(dom);
			// bd搜索分为2步
			if (type == 1) {
				// 第一步，获取城市代码c和用户uid
				JSONObject current_city = object.getJSONObject("current_city");
				String c = String.valueOf(current_city.get("code"));

				JSONObject result = object.getJSONObject("result");
				String uid = String.valueOf(result.get("profile_uid"));

				return uid + "," + c;

			} else {
				// 第二部，获取返回数据
				JSONObject content = object.getJSONObject("content");
				String geo = content.getString("geo");
				if (!"".equals(geo)) {
					String str = geo.split("\\|")[2].substring(2);
					return str.substring(0, str.lastIndexOf(";"));
				}
			}
		} catch( Exception e) {
			e.printStackTrace();
			log.error(e);
			return null;
		}
		return null;
	}
}
