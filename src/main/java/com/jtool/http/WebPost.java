package com.jtool.http;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtool.http.exception.HttpRequestException;
import com.jtool.http.exception.StatusCodeNot200Exception;

public class WebPost extends AbstractWebRequest {

	protected static Logger logger = LoggerFactory.getLogger(WebPost.class);

	public static String sent(String url, Object bean) {
		return sent(url, converBeanToRequestMap(bean));
	}

	public static String sent(String url, Map<String, ?> params) {
		String result = (String)doSent(url, params, ReturnType.string);
		logger.debug("返回结果：" + result);
		return result;
	}
	public static byte[] sentBytes(String url, Map<String, ?> params) {
		byte[] result = (byte[])doSent(url, params, ReturnType.bytes);
		return result;
	}

	private static HttpEntity converParamsToEntity(Map<String, ?> params) {

		if (hasAccessories(params)) {
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

			for (Entry<String, ?> entry : params.entrySet()) {

				String key = entry.getKey();

				if (entry.getValue() instanceof File) {
					File file = (File) entry.getValue();
					addFileMultipartEntity(multipartEntityBuilder, key, file);
				} else {
					multipartEntityBuilder.addPart(key, new StringBody(entry.getValue().toString(), ContentType.create("text/plain", Consts.UTF_8)));
				}
			}

			return multipartEntityBuilder.build();
		} else {
			List<NameValuePair> nameValuePairList = converParamsToNameValuePairs(params);
			return new UrlEncodedFormEntity(nameValuePairList, Consts.UTF_8);
		}

	}

	private static void addFileMultipartEntity(MultipartEntityBuilder multipartEntityBuilder, String key, File file) {
		String exString = FilenameUtils.getExtension(file.getName()).toLowerCase();

		switch (exString) {
		case "jpg":
		case "jpeg":
			multipartEntityBuilder.addBinaryBody(key, file, ContentType.create("image/jpeg"), file.getName());
			break;
		case "gif":
			multipartEntityBuilder.addBinaryBody(key, file, ContentType.create("image/gif"), file.getName());
			break;
		case "png":
			multipartEntityBuilder.addBinaryBody(key, file, ContentType.create("image/png"), file.getName());
			break;
		default:
			multipartEntityBuilder.addPart(key, new FileBody(file));
			break;
		}
	}

	private static boolean hasAccessories(Map<String, ?> params) {
		return params.keySet().stream().anyMatch(e -> params.get(e) instanceof File);
	}

	public static byte[] sentAndReturnBytes(String url, Object bean) {
		return sentAndReturnBytes(url, converBeanToRequestMap(bean));
	}

	public static byte[] sentAndReturnBytes(String url, Map<String, ?> params) {
		return (byte[])doSent(url, params, ReturnType.bytes);
	}

	private static Object doSent(String url, Map<String, ?> params, String returnType) {
		logger.debug("发送url:" + url);
		logger.debug("发送params:" + params.toString());

		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(converParamsToEntity(params));

			httpPost.addHeader("Accept-Encoding", "*");
			
			HttpClientBuilder builder = HttpClientBuilder.create();
			try (CloseableHttpClient httpclient = builder.build(); CloseableHttpResponse response = httpclient.execute(httpPost)) {
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
				} else if(300 < statusCode && statusCode < 310) {
					return WebPost.sent(response.getLastHeader("Location").getValue(), params);
				} else {
					logger.debug("StatusCodeNot200Exception: " + statusCode + " url:" + url);
					throw new StatusCodeNot200Exception(url, params, statusCode);
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
