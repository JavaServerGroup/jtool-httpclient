package com.jtool.http;

import static com.jtool.http.RemoteTestUtils.port;
import static com.jtool.http.RemoteTestUtils.root;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.jtool.http.exception.HttpRequestException;
import com.jtool.http.exception.StatusCodeNot200Exception;

public class WebPostTest extends AbstractRequestTest {

	@Test
	public void testSentByMap() {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = makeParams();
		
		String url = root(port()) + postUri;
		String result = WebPost.sent(url, params);
		
		Assert.assertEquals(this.postResponse, result);
	}
	
	@Test
	public void testSentByBean() {
		User user = makeUserBean();
		
		String url = root(port()) + postUri;
		String result = WebPost.sent(url, user);
		
		Assert.assertEquals(this.postResponse, result);
	}
	
	@Test
	public void testSentByBeanAndReturnBytes() {
		User user = makeUserBean();
		
		String url = root(port()) + postUri;
		byte[] result = WebPost.sentAndReturnBytes(url, user);
		
		Assert.assertArrayEquals(this.postResponse.getBytes(), result);
	}
	
	@Test
	public void testSentByMapAndRecirected() {
		Map<String, String> params = new HashMap<>();
		params.put("redirectParamName", "redirectParamValue");
		
		String url = root(port()) + "/redirectPost";
		String result = WebPost.sent(url, params);
		
		Assert.assertEquals("redirectedPostSuccess", result);
	}
	
	@Test(expected=StatusCodeNot200Exception.class)
	public void testNot200Response() {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = makeParams();
		String url = root(port()) + "/notExitPathTo404";
		WebPost.sent(url, params);
	}
	
	@Test(expected=HttpRequestException.class)
	public void testIoException() {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = makeParams();
		String url = "http://not-a-url";
		WebPost.sent(url, params);
	}
	

}

