package com.jtool.http;

import com.jtool.http.exception.RequestBeanErrorException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonCommand {

	private static Logger log = LoggerFactory.getLogger(CommonCommand.class);

	public static String readRequest(String host, String uri, Map<String, String> map) {

		if(log.isDebugEnabled()) {
			log.debug("curl '" + host + uri + "?" + join(getParamStrings(map)) + "'");
		}

		String content;

		int i = 0;

		do {
			content = WebGet.sent(host + uri, map);
			i++;
		} while ((content == null || content.equals("")) && i < 5);

		log.debug("response: " + content);

		return content;
	}

	public static String writeRequest(String host, String uri, Map<String, String> map) {

		if(log.isDebugEnabled()) {
			log.debug("curl -X POST -d \"" + join(getParamStrings(map)) + "\" " + host + uri);
		}

		String content = WebPost.sent(host + uri, map);

		log.debug("response: " + content);

		return content;
	}

	public static String readRequest(String host, String uri, Object param) {
		return readRequest(host, uri, convertBeanToRequestMap(param));
	}

	public static String writeRequest(String host, String uri, Object param) {
		return writeRequest(host, uri, convertBeanToRequestMap(param));
	}

	private static List<String> getParamStrings(Map<String, ?> paramsMap) {
		List<String> paramsList = new ArrayList<>();
		for(String key : paramsMap.keySet()){
            try {
                if(paramsMap.get(key) != null) {
                    paramsList.add(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(paramsMap.get(key).toString(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
		return paramsList;
	}

	private static Map<String, String> convertBeanToRequestMap(Object bean) {
		Map<String, String> params;
		try {
			params = BeanUtils.describe(bean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RequestBeanErrorException(e);
		}
		params.remove("class");
		return params;
	}

	private static String join(final List<String> params){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < params.size(); i++){
			if(i != 0) {
				sb.append("&");
			}
			sb.append(params.get(i));
		}
		return sb.toString();
	}
}
