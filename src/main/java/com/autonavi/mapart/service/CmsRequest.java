package com.autonavi.mapart.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.autonavi.mapart.entity.CmsData;
import com.autonavi.mapart.entity.Cms_Record;
import com.autonavi.mapart.orm.Cms_RecordDao;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
public class CmsRequest {
	 private static Logger log = Logger.getLogger("CMSRequest");
	
	 public static void main(String[] args) {
//         startMsgServer();
        
	 }
   public static boolean startMsgServer(){
	   log.debug("http server monitor start...");
         try{
               HttpServerProvider provider = HttpServerProvider.provider();
               HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(10011), 10);
               httpserver.createContext("/cmsreq", new MyHttpHandler());
               httpserver.setExecutor(null);
               httpserver.start();
         }catch(Exception e){
               e.printStackTrace();
               return false;
         }
         return true;
   }
   
 
 static class MyHttpHandler implements HttpHandler {  
	 static int[] fa_ids;
	 private int start =0 ;
	 private int length=0;
	 private List<CmsData> list ;
	 private FacilityareaDao facilityareaDao;
     public void handle(HttpExchange httpExchange) throws IOException{  
    	  OutputStream out = httpExchange.getResponseBody();
           try{
        	   httpExchange.sendResponseHeaders(200, 0L);                  
        	   httpExchange.getResponseHeaders().set("Content-Type","text;charset=UTF-8");
	            String url = httpExchange.getRequestURI().toString();
	            log.debug("客户端请求的URL是：　　"+url);
	            String urlTrue = checkUrl(url);
	            if(!urlTrue.equals("")){
	            	out.write(urlTrue.getBytes());
	            	return ;
	            }
	            BeanFactory factory = new ClassPathXmlApplicationContext("service-context.xml");
	    		String jsonStr = getCmsData(factory);
	    		log.debug("入cms库的json串： "+jsonStr);
                out.write(jsonStr.getBytes());
                if(!jsonStr.equals("没有符合条件的数据。。。。")){
                	recordCmsInfo(factory);
                }
             }catch(Exception e){
                 e.printStackTrace();
                 log.debug("cms 请求入库的异常："+e.getMessage());
           }finally{
        	   out.close();
           }
     }
     /**
      * 
      * 检测请求的URL是否正确
      */
     private String checkUrl(String url){
    	 if(!url.contains("&s=")||!url.contains("&l=")){
         	return " url 不正确。。。。。";
         }
         start = Integer.parseInt(url.substring(url.indexOf("&s=")+3, url.indexOf("&l=")));
         length = Integer.parseInt(url.substring(url.indexOf("&l=")+3));
         log.debug("获取数据的起点位置："+start +"   获取数据的长度"+length);
         if(start <=0 || length<=0){
         	return "步长或者起点不正确。。。。。";
         }
         fa_ids = new int[length];
         for(int i=0;i<length;i++){
         	fa_ids[i]=start+i;
         	log.debug("cms请求的gid  "+i+":"+fa_ids[i]);
         }
         return "";
     }
     /**
      * 根据流水号(fa_id)查询数据
      * @param factory
      * @return
      */
     private String getCmsData(BeanFactory factory){
    	 facilityareaDao = (FacilityareaDao)factory.getBean("facDao");
         log.debug("facilityareaDao===="+facilityareaDao);
         list = facilityareaDao.getCmsData(fa_ids);
 		 log.debug("符合入cms库的数量==================================="+list.size());
 		 if(list.size()<=0){
			return "没有符合条件的数据。。。。";
		 }
 		return JSONArray.fromObject(list).toString().replaceAll("\"callbacks\":\\[\\{\\}\\],", "");
     }
     /**
      * 
      * 更新入cms库的状态（release）
      * 保存cms请求信息
      * @param factory
      */
     private void recordCmsInfo(BeanFactory factory){
    	 facilityareaDao.updateReleaseByGid(list);
         Cms_RecordDao cms_record = (Cms_RecordDao)factory.getBean("cms_record");
         cms_record.insertCmsRecord(new Cms_Record(start,length,list.get(list.size()-1).getFa_id()));

     }
 }

}
