/**
 * @projectName:dhtserver
 * @packageName:com.dht.base.util
 * @className:HttpClientUtils.java
 *
 * @createTime:2014年3月13日-下午2:13:40
 *
 *
 */
package com.yucl.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTT请求处理类
 * 
 * @author hongxiaodong
 */
public class HttpClientUtils {
	private static Logger LOGGER = LoggerFactory
			.getLogger(HttpClientUtils.class);

	public static String doGet(String url) {
		String result = null;
		try {
			HttpClient httpClient = HttpClients.createDefault();
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数

			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			result = EntityUtils.toString(entity);
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;

	}

	public static String doPost(String url, List<NameValuePair> params) {
		String result = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
		} catch (Exception e1) {

			e1.printStackTrace();
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;

	}

	/**
	 * HTTP请求处理
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String readContentFromGet(String url,
			Map<String, String> params) throws IOException {

		StringBuffer buffer_URL = new StringBuffer(url);
		Set<String> pas = params.keySet();
		List<String> list = new ArrayList<String>();
		list.addAll(pas);
		for (int i = 0; i < list.size(); i++) {
			String pa = list.get(i);
			String va = params.get(pa);
			if (i == 0) {
				buffer_URL.append("?").append(pa).append("=")
						.append(URLEncoder.encode(va, "utf-8"));
			} else {
				buffer_URL.append("&").append(pa).append("=")
						.append(URLEncoder.encode(va, "utf-8"));
			}
		}
		URL getUrl = new URL(buffer_URL.toString());
		HttpURLConnection connection = (HttpURLConnection) getUrl
				.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuffer lines = new StringBuffer("");
		String string;
		while ((string = reader.readLine()) != null) {
			System.out.println(string);
			lines.append(string);
		}
		reader.close();
		connection.disconnect();
		return lines.toString();
	}

	public static String httpPostBody(String url, Object params) {
		String body = null;
		if (url != null && params != null) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = httpPost(url, params);
			HttpResponse response = null;
			try {
				response = httpclient.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			String charset = EntityUtils.getContentCharSet(entity);
			try {
				body = EntityUtils.toString(entity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return body;
	}

	private static HttpPost httpPost(String url, Object params) {
		if (url != null && params != null) {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params instanceof Map) {
				Map<String, String> paramsMap = (Map<String, String>) params;
				Set<String> keySet = paramsMap.keySet();
				for (String key : keySet) {
					if (StringUtils.isNotEmpty(paramsMap.get(key))){
						nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
					}
				}
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				return httpPost;
			} else if (params instanceof String) {
				httpPost.addHeader("Content-type",
						"application/json; charset=utf-8");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(new StringEntity(params.toString(), Charset
						.forName("UTF-8")));
				return httpPost;
			}
		}
		return null;
	}


	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String httpGetBody(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}



	/***********************************************新的方法**************************************************/

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

}
