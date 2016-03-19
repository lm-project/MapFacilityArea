package com.autonavi.mapart.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class XidRequest {
	 private static Logger  log  = Logger.getLogger("XidRequest");
	 
	 public static String sendPost(String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        String url = "http://172.16.21.44:6003/xid/getXidByPoiIdAndMeshId.shtml";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	    	    HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setRequestMethod("POST");// 设置提交方法
	            conn.setConnectTimeout(50000);// 连接超时时间
	            conn.setReadTimeout(50000);
	            conn.setDoOutput(true);// 打开写入属性
	            conn.setDoInput(true);// 打开读取属性
	            
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            out.print(param);// 发送请求参数
	            out.flush();// flush输出流的缓冲
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	        	log.error("请求xid异常："+e.getMessage());
	            e.printStackTrace();
	        }finally{//使用finally块来关闭输出流、输入流
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    
}
