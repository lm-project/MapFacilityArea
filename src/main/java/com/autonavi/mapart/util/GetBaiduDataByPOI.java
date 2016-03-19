/*package com.autonavi.mapart.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetBaiduDataByPOI {

	private static final String SEARCH_POI = "http://map.baidu.com/?newmap=1&reqflag=pcmap&"
			+ "biz=1&from=webmap&qt=s&da_src=pcmappg.searchBox.button&wd=小区&c=131"
			+ "&src=0&wd2=&sug=0&l=15&b=(12849780.03,4758298.57;13081648.69,4993877.7)"
			+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t=1409818765064";
	
	
	private static final String SEARCH_POI_URL = "http://gss3.map.baidu.com/?"
			+ "newmap=1&reqflag=pcmap&biz=1&from=webmap"
			+ "&qt=bkg_data&c=131&ie=utf-8&wd=#SEARCH_KEY#&l=15"
			+ "&xy=#TITLE_POINT#"
			+ "&b=(#BBOX#)";
	
	
	private Log log = LogFactory.getLog("myLog");
	StringBuffer sb = new StringBuffer();
	//(12925448.96,4809355.72;12941688.96,4816827.72)
	private static final double MAX_LNG = 13081648.69;
	private static final double MAX_LAT = 4993877.7;
	private static double MIN_LNG = 12925448.96;
	private static double MIN_LAT = 4809355.72;
	
	public void  getPoiURL(String key){
		Collection<String> tile_xy = new ArrayList<String>();
		
		try {
			int bbox_x = (int) Math.ceil(( MAX_LNG - MIN_LNG )/8120);
			int bbox_y = (int) Math.ceil(( MAX_LAT - MIN_LAT )/2296);
			log.info("box"+bbox_x+","+bbox_y);
			double pixel =  Math.pow(2,15-18);
			for(int bx = 0; bx < bbox_x; bx++){//bbox_x
				int MIN_TILE_X = (int) Math.floor(MIN_LNG*pixel/256);
				int MAX_TILE_X = (int) Math.floor((MIN_LNG+8210)*pixel/256);
				for(int by = 0; by < bbox_y; by++){//bbox_y
					
					String bbox = String.format("%20.2f", MIN_LNG)+","+String.format("%20.2f", MIN_LAT)+";"
							+String.format("%20.2f", MIN_LNG+8120)+","+String.format("%20.2f", MIN_LAT+2296);
					
					int MIN_TILE_Y = (int) Math.floor(MIN_LAT*pixel/256);
					int MAX_TILE_Y = (int) Math.floor((MIN_LAT+1280)*pixel/256);
					
					log.info(MIN_TILE_X+","+MAX_TILE_X+";"+MIN_TILE_Y+","+MAX_TILE_Y);
					tile_xy.add(bbox);
					
					
					for(int i = MIN_TILE_X; i<=MAX_TILE_X; i++){//tile_x
						
						for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j++){//tile_y
							String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", key)
								   .replaceAll("#TITLE_POINT#",i+"_"+j)
						           .replace("#BBOX#",bbox)
						           .replace(" ", "");
							
							Document doc = requestURL(urlString);
							
							if(doc==null){
								log.debug("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
							}else{
								
								log.info("============================================");
								JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
								log.info(json);
								JSONArray array = json.getJSONArray("uids");
								for(int m = 0; m< array.size() ; m ++) {
									JSONObject poi = array.getJSONObject(m);
									log.info("==========="+poi.getString("name"));
								}
							}
						}
					}
					MIN_LAT = MIN_LAT+2296;
				}
				MIN_LNG = MIN_LNG+8120;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document requestURL(String urlString) throws Exception {
		try {
			return Jsoup.parse(new URL(urlString), 10 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}*/

package com.autonavi.mapart.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetBaiduDataByPOI {

	@SuppressWarnings("unused")
	private static final String SEARCH_POI = "http://map.baidu.com/?newmap=1&reqflag=pcmap&"
			+ "biz=1&from=webmap&qt=s&da_src=pcmappg.searchBox.button&wd=小区&c=131"
			+ "&src=0&wd2=&sug=0&l=15&b=(12849780.03,4758298.57;13081648.69,4993877.7)"
			+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t=1409818765064";
	
	
	@SuppressWarnings("unused")
	private static final String SEARCH_POI_URL = "http://gss3.map.baidu.com/?"
			+ "newmap=1&reqflag=pcmap&biz=1&from=webmap"
			+ "&qt=bkg_data&c=131&ie=utf-8&wd=#SEARCH_KEY#&l=15"
			+ "&xy=#TITLE_POINT#"
			+ "&b=(#BBOX#)";
	
	
	private Log log = LogFactory.getLog("myLog");
	StringBuffer sb = new StringBuffer();
	//(12925448.96,4809355.72;12941688.96,4816827.72)
	private static final double MAX_LNG = 13081648.69;
	private static final double MAX_LAT = 4993877.7;
	private static double MIN_LNG = 12925448.96;
	private static double MIN_LAT = 4809355.72;
	
	public void  getPoiURL(String key){
		Collection<String> boxCollection = new ArrayList<String>();
		
		try {
			int bbox_x = (int) Math.ceil(( MAX_LNG - MIN_LNG )/8120);
			int bbox_y = (int) Math.ceil(( MAX_LAT - MIN_LAT )/2296);
			log.info("box"+bbox_x+","+bbox_y);
			for(int bx = 0; bx < bbox_x; bx++){//bbox_x
				for(int by = 0; by < bbox_y; by++){//bbox_y
					String bbox = (String.format("%20.2f", MIN_LNG)+","+String.format("%20.2f", MIN_LAT)+";"
				          +String.format("%20.2f", MIN_LNG+8210)+","+String.format("%20.2f", MIN_LAT+2296)).replace(" ", "");
					boxCollection.add(bbox);
					MIN_LAT = MIN_LAT+2296;
				}
				MIN_LNG = MIN_LNG+8120;
			}
			getTileXY(boxCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getTileXY(Collection<String> boxCollection){
		 JSONArray _json=JSONArray.fromObject(boxCollection);
		 double pixel = Math.pow(2,15-18);
		 String JsonText = "";
		 for(int m = 0; m<_json.size(); m++){
			 String[] boxArr = _json.get(m).toString().split(";");
			 String[] boxLeftDownPoi = boxArr[0].split(",");
			 String[] boxRightUpPoi = boxArr[1].split(",");
			 int MIN_TILE_X = (int) Math.floor(Double.parseDouble(boxLeftDownPoi[0])*pixel/256);
			 int MIN_TILE_Y = (int) Math.floor(Double.parseDouble(boxLeftDownPoi[1])*pixel/256);
			 int MAX_TILE_X = (int) Math.floor((Double.parseDouble(boxRightUpPoi[0])+8210)*pixel/256);
			 int MAX_TILE_Y = (int) Math.floor((Double.parseDouble(boxRightUpPoi[1])+2296)*pixel/256);
			 String tileJsonText="[";
			 for(int i = MIN_TILE_X; i<=MAX_TILE_X; i++){
				 for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j++){//tile_y
					 tileJsonText+="{\"xy\":\""+i+"_"+j+"\"},";
				 }
			 }
			JsonText +=  tileJsonText.substring(0,tileJsonText.length()-1)+"],";
		 }
		 System.out.println(JsonText.substring(0,JsonText.length()-1));
		 JSONArray c_json=JSONArray.fromObject(JsonText);
		 System.out.println("================================================================");
		 System.out.println(c_json);
	}
	
	
	@SuppressWarnings("unused")
	private Document requestURL(String urlString) throws Exception {
		try {
			return Jsoup.parse(new URL(urlString), 10 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*int MIN_TILE_Y = (int) Math.floor(MIN_LAT*pixel/256);
	int MAX_TILE_Y = (int) Math.floor((MIN_LAT+1280)*pixel/256);
	
	log.info(MIN_TILE_X+","+MAX_TILE_X+";"+MIN_TILE_Y+","+MAX_TILE_Y);
	
	
	for(int i = MIN_TILE_X; i<=MAX_TILE_X; i++){//tile_x
		
		for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j++){//tile_y
			String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", key)
				   .replaceAll("#TITLE_POINT#",i+"_"+j)
		           .replace("#BBOX#",bbox)
		           .replace(" ", "");
			
			Document doc = requestURL(urlString);
			
			if(doc==null){
				log.debug("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
			}else{
				
				log.info("============================================");
				JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
				log.info(json);
				JSONArray array = json.getJSONArray("uids");
				for(int m = 0; m< array.size() ; m ++) {
					JSONObject poi = array.getJSONObject(m);
					log.info("==========="+poi.getString("name"));
				}
			}
		}*/
	
	
	
}

 

