package com.jtool.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jtool.http.exception.RequestBeanErrorException;

public abstract class AbstractWebRequest {

	protected static Map<String, String> converBeanToRequestMap(Object bean) {
		Map<String, String> params;
		try {
			params = BeanUtils.describe(bean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RequestBeanErrorException(e);
		}
		params.remove("class");
		return params;
	}

	protected static List<NameValuePair> converParamsToNameValuePairs(Map<String, ?> params) {
		return params.keySet().stream().filter(e -> params.get(e) != null).map(e -> new BasicNameValuePair(e, params.get(e).toString())).collect(Collectors.toList());
	}

	protected static byte[] getResponseBytes(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		return EntityUtils.toByteArray(entity);
	}

	protected static String getResponseString(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, Consts.UTF_8);
		return result;
	}

	protected static boolean isSuccess(int statusCode) {
		return statusCode == 200;
	}

}
