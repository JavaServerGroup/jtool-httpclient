package com.jtool.http;

import org.junit.Assert;
import org.junit.Test;

public class WebTest extends AbstractRequestTest {
	
	@Test
	public void test() throws ClassNotFoundException {
		Class.forName(WebGet.class.getName());
		Class.forName(WebPost.class.getName());
		Assert.assertNotEquals(WebGet.logger, WebPost.logger);
	}

	
}
