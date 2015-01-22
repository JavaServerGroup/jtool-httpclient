package com.jtool.http;

import static com.jtool.http.RemoteTestUtils.port;
import static com.jtool.http.RemoteTestUtils.root;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.jtool.http.exception.HttpRequestException;
import com.jtool.http.exception.StatusCodeNot200Exception;

public class WebGetTest extends AbstractRequestTest {
	
	@Test
	public void testSentByMap() {
		@SuppressWarnings("unchecked")
		Map<String, String> params = makeParams();
		params.put("nullKey", null);
		
		String url = root(port()) + getWithParamsUri;
		String result = WebGet.sent(url, params);
		
		Assert.assertEquals(this.getWithParamResponse, result);
	}
	
	@Test
	public void testSentByBean() {
		User user = makeUserBean();
		
		String url = root(port()) + getWithParamsUri;
		String result = WebGet.sent(url, user);
		
		Assert.assertEquals(this.getWithParamResponse, result);
	}
	
	@Test(expected=StatusCodeNot200Exception.class)
	public void testNot200Response() {
		String url = root(port()) + "/notExitPathTo404";
		WebGet.sent(url);
	}
	
	@Test(expected=HttpRequestException.class)
	public void testIoException() {
		String url = "http://not-a-url";
		WebGet.sent(url);
	}

	@Test
	public void testSentString() {
		String url = root(port()) + getUri;
		String result = WebGet.sent(url);
		Assert.assertEquals(result, this.getResponse);
	}
	
}
