package com.jtool.http;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jtool.http.RemoteTestUtils.port;
import static com.jtool.http.RemoteTestUtils.root;

public class CommonCommandTest extends AbstractRequestTest {

	@Test
	public void testReadRequest() {
		String result = CommonCommand.readRequest(root(port()), getWithParamsUri, makeParams());
		Assert.assertEquals(this.getWithParamResponse, result);
	}
	
	@Test
	public void testWriteRequest() {
		String result = CommonCommand.writeRequest(root(port()), postUri, makeParams());
		Assert.assertEquals(this.postResponse, result);
	}

	public Map<String, String> makeParams() {
		Map<String, String> params = new HashMap<>();
		params.put(paramName, paramValue);
		return params;
	}

}

