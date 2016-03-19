package com.autonavi.mapart.util;

import java.io.IOException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.autonavi.mapart.entity.ResponseStatus;

/**
 * http连接、抓取管理类
 * 
 * @author qiang.cai
 * 
 */

public class HttpConnectionManager {
	public static int m = 0;
	/**
	 * 
	 * 连接池里的最大连接数
	 */
	public static final int MAX_TOTAL_CONNECTIONS = 100;

	/**
	 * 
	 * 每个路由的默认最大连接数
	 */
	public static final int MAX_ROUTE_CONNECTIONS = 50;

	/**
	 * 
	 * 连接超时时间
	 */
	public static final int CONNECT_TIMEOUT = 50000;

	/**
	 * 
	 * 套接字超时时间
	 */
	public static final int SOCKET_TIMEOUT = 50000;

	/**
	 * 
	 * 连接池中 连接请求执行被阻塞的超时时间
	 */
	public static final long CONN_MANAGER_TIMEOUT = 60000;

	/**
	 * 
	 * http连接相关参数
	 */
	private static HttpParams parentParams;

	/**
	 * 
	 * http线程池管理器
	 */
	private static PoolingClientConnectionManager cm;

	/**
	 * 
	 * http客户端
	 */
	private static DefaultHttpClient httpClient;

	/**
	 * 
	 * 默认目标主机
	 */
	private static final HttpHost DEFAULT_TARGETHOST = new HttpHost(
			"http://route.map.qq.com", 80);

	/**
	 * 
	 * 初始化http连接池，设置参数、http头等等信息
	 */
	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		cm.setMaxPerRoute(new HttpRoute(DEFAULT_TARGETHOST), 20); // 设置对目标主机的最大连接数
		parentParams = new BasicHttpParams();
		parentParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		parentParams
				.setParameter(ClientPNames.DEFAULT_HOST, DEFAULT_TARGETHOST); // 设置默认targetHost
		parentParams.setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT,
				CONN_MANAGER_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				CONNECT_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT,
				SOCKET_TIMEOUT);
		parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
//		parentParams.setParameter(ClientPNames.DEFAULT_HEADERS, collection);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 5) {
					// 如果超过最大重试次数，那么就不要继续了
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// 如果服务器丢掉了连接，那么就重试
					return true;
				}

				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);

				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求被认为是幂等的，那么就重试
					return true;
				}
				return false;
			}
		};
		httpClient = new DefaultHttpClient(cm, parentParams);
		httpClient.setHttpRequestRetryHandler(httpRequestRetryHandler);
	}

	/**
	 * 
	 * 抓取url所指的页面代码
	 * 
	 * @param url
	 *            目标页面的url
	 * 
	 * @return 页面代码
	 */
	public static ResponseStatus getHtml(String url) {
		String html = "";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;
		HttpEntity httpEntity;
		try {
			httpResponse = httpClient.execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (200 != statusCode) {
				return new ResponseStatus(300, "false", "访问被拒绝!");
			}
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				html = readHtmlContentFromEntity(httpEntity);
				return new ResponseStatus(100, html==null || "".equals(html) ?"" : html , "success");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new ResponseStatus(300, "false", "访问被拒绝!");
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseStatus(300, "false", "访问被拒绝!");
			
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return new ResponseStatus(100, html==null || "".equals(html) ?"" : html , "success");
	}

	/**
	 * 
	 * 从response返回的实体中读取页面代码
	 * 
	 * @param httpEntity
	 *            Http实体
	 * 
	 * @return 页面代码
	 * 
	 * @throws ParseException
	 * 
	 * @throws IOException
	 */

	private static String readHtmlContentFromEntity(HttpEntity httpEntity)
			throws ParseException, IOException {

		String html = "";
		Header header = httpEntity.getContentEncoding();
		if (httpEntity.getContentLength() < 2147483647L) { // EntityUtils无法处理ContentLength超过2147483647L的Entity
			if (header != null && "gzip".equals(header.getValue())) {
				html = EntityUtils.toString(new GzipDecompressingEntity(
						httpEntity));
			} else {
				html = EntityUtils.toString(httpEntity);
			}
		} 
		String resultString = html.substring(
				html.indexOf("(") + 1, html.lastIndexOf(")"));
		return executeDom(resultString);
	}

	/**
	 * 查询关键字所在设施区域
	 * 
	 * @param key
	 * @return
	 */
	public static ResponseStatus select(String key) {
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
				System.out.println(keys[0] + keys[1]);
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
	private static ResponseStatus request(String enkey) {
		String urlString = "http://route.map.qq.com/?qt=poi&c=3&wd="
				+ enkey
				+ "&pn=0&rn=9&bl=1&nj=0&nr=0&nf=1&l=15&"
				+ "rl=9&lc=1&nqc=0&owd=&qct=&sid=cdf9df31415e095fa6ac57044004baa9&output=jsonp&cb=QQMapLoader.cbhudd5mwo8";
		ResponseStatus status = getHtml(urlString);
		return status;
	}

	// 解析DOM元素
	private static String executeDom(String dom) {
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
			m++;
			System.err.println(e);
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

		public Test(String key) {
			this.key = key;
		}

		public void run() {
			System.out.println(select(key));
		}
	}

	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws InterruptedException {

		HttpConnectionManager httpConnectionManager = new HttpConnectionManager();

		Date start = new Date();
		// System.out.println(httpConnectionManager.getHtml("http://map.baidu.com"));

		HttpConnectionManager.Test tes1 = httpConnectionManager.new Test("北京大学");
		for (int i = 0; i < 10000; i++) {
			new Thread(tes1).run();
		}
		Date end = new Date();
		System.out.println("执行错误数："+ m + "," + (end.getTime() - start.getTime()) / 1000.0 + " 秒");

	}

}