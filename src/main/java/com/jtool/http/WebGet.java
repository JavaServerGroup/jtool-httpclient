package com.jtool.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtool.http.exception.HttpRequestException;
import com.jtool.http.exception.StatusCodeNot200Exception;

public class WebGet extends AbstractWebRequest {

	protected static Logger logger = LoggerFactory.getLogger(WebGet.class);
	
	public static String sent(String url, Object bean) {
		return sent(url, converBeanToRequestMap(bean));
	}
	
	public static String sent(String url, Map<String, String> params) {
		
		logger.debug("发送url:" + url);
		logger.debug("发送params:" + params);
		
		if (params != null) {
			url = assembleParamsToUrl(url, params);
		}
		return sent(url);
		
	}
	
	public static byte[] sentAndReturnBytes(String url, Object bean) {
		return sentAndReturnBytes(url, converBeanToRequestMap(bean));
	}
	
	public static byte[] sentAndReturnBytes(String url, Map<String, String> params) {
		return (byte[])doSent(url, params, ReturnType.bytes);
	}

	private static String assembleParamsToUrl(String url, Map<String, String> params) {
		List<NameValuePair> nameValuePairList = converParamsToNameValuePairs(params);
		String querystring = URLEncodedUtils.format(nameValuePairList, Consts.UTF_8);
		return url + "?" + querystring;
	}
	
	public static String sent(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept-Encoding", "*");
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		try (CloseableHttpClient httpclient = builder.build();CloseableHttpResponse response = httpclient.execute(httpGet)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (isSuccess(statusCode)) {
				String result = getResponseString(response);
				logger.debug("返回结果：" + result);
				return result;
			} else if(300 < statusCode && statusCode < 310) {
				return WebGet.sent(response.getLastHeader("Location").getValue());
			} else {
				logger.debug("StatusCodeNot200Exception: " + statusCode + " url:" + url);
				throw new StatusCodeNot200Exception(url, statusCode);
			}
		} catch (IOException e) {
			throw new HttpRequestException(url, e);
		}
	}
	
	private static Object doSent(String url, Map<String, String> params, String returnType) {
		logger.debug("发送url:" + url);
		logger.debug("发送params:" + params);
		
		if (params != null) {
			url = assembleParamsToUrl(url, params);
		}

		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept-Encoding", "*");
			
			HttpClientBuilder builder = HttpClientBuilder.create();
			try (CloseableHttpClient httpclient = builder.build(); CloseableHttpResponse response = httpclient.execute(httpGet)) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (isSuccess(statusCode)) {
					switch (returnType) {
					case ReturnType.bytes:
						return getResponseBytes(response);
					case ReturnType.string:
						return getResponseString(response);
					default:
						throw new RuntimeException();
					}
				} else {
					logger.debug("StatusCodeNot200Exception: " + statusCode + " url:" + url);
					throw new StatusCodeNot200Exception(url, statusCode);
				}
			}

		} catch (IOException e) {
			throw new HttpRequestException(url, e);
		}
	}

	private static class ReturnType{
		private static final String string = "string";
		private static final String bytes = "bytes";
	}

}
