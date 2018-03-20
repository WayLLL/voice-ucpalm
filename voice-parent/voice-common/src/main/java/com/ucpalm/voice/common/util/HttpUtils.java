package com.ucpalm.voice.common.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * http请求基础工具
 * @author chendi
 *
 */
public class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private static final int SO_TIMEOUT = 30 * 1000;
	private static final int SO_TIMEOUT_1 = 60 * 1000;
	
	/**
	 * get请求 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> params) {
		CloseableHttpClient httpClient =  HttpClients.createDefault();
		String result = "";
		HttpGet httpGet = new HttpGet(url);
		StringBuffer param = new StringBuffer();
	    int i = 0;
	    for ( String key : params.keySet() ) {
	         if ( i == 0 )
	           param.append( "?" );
	        else
	          param.append( "&" );
	          param.append( key ).append( "=" ).append( params.get( key ) );
	          i++;
	     }
	        url += param;
		logger.info("get请求，url = {}, 请求的参数  = {}", url, params);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			} else {
					logger.error("请求：" + url + "，失败，返回码:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("请求：{}失败,异常信息 message={}", url,e.getMessage());
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("关闭httpClient失败，e={}", e);
			}
		}
		return result;
	}
	
	/**
	 * post 请求
	 * @param url 请求地址
	 * @param jsonStr 请求参数 json 字符串
	 * @return
	 */
	public static String sendPostJson(String url, String jsonStr) {
		CloseableHttpClient httpClient =  HttpClients.createDefault();
		String result = "";
		/**
		 * 设置请求超时配置
		 */
		RequestConfig requestConfig =RequestConfig.custom().setSocketTimeout(SO_TIMEOUT)
										.setConnectTimeout(SO_TIMEOUT).setConnectionRequestTimeout(SO_TIMEOUT_1).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new  StringEntity(jsonStr, "UTF-8"));
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.addHeader("Accept", "application/json");
		httpPost.setConfig(requestConfig);
		logger.info("post请求，url = {}, 请求的参数  = {}", url, jsonStr);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
				printOutBoundMessage(response, result);
			} else {
					logger.error("请求：" + url + "，失败，返回码:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("请求：{}失败,异常信息 message={}", url,e.getMessage());
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("关闭httpClient失败，e={}", e);
			}
		}
		return result;
	}

	

	public static String sendMessageXml(String url, String xmlStr) {
		CloseableHttpClient httpClient =  HttpClients.createDefault();
		String result = "";
		/**
		 * 设置请求超时配置
		 */
		RequestConfig requestConfig =RequestConfig.custom().setSocketTimeout(SO_TIMEOUT)
										.setConnectTimeout(SO_TIMEOUT).setConnectionRequestTimeout(SO_TIMEOUT_1).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new  StringEntity(xmlStr, "UTF-8"));
		httpPost.setHeader("Content-Type", "application/xml;charset=utf-8");
		httpPost.addHeader("Accept", "application/json");
		httpPost.setConfig(requestConfig);
		logger.info("post请求，url = {}, 请求的参数  = {}", url, xmlStr);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
				printOutBoundMessage(response, result);
			} else {
					logger.error("请求：" + url + "，失败，返回码:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("请求：{}失败,异常信息 message={}", url,e.getMessage());
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("关闭httpClient失败，e={}", e);
			}
		}
		return result;
	}

	private static void printOutBoundMessage(HttpResponse response, String responseString) {
		StringBuffer info = new StringBuffer("\n---------------------------\nOutbound Message\n");
		info.append("ID: ").append(Log4jUtils.getLogId()).append("\n");

		info.append("Content-Type: ").append(response.getHeaders("content-type")).append("\n");

		info.append("Headers: ").append(response.getAllHeaders()).append("\n");

		info.append("Messages: ").append(responseString).append("\n---------------------------");

		logger.debug("http请求日志打印信息: {}", info);
	}

	
	/**
     * 获取 包装防Xss Sql注入的 HttpServletRequest
     * @return request
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
	
	/**
	 * 获取请求ip地址
	 * @param request
	 * @param ctx
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String clientIP = request.getRemoteAddr();
		return clientIP;
	}
}
