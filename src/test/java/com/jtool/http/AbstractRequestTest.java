package com.jtool.http;

import static com.github.dreamhead.moco.Moco.and;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.eq;
import static com.github.dreamhead.moco.Moco.form;
import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.query;
import static com.github.dreamhead.moco.Moco.uri;
import static com.jtool.http.RemoteTestUtils.port;
import static com.jtool.http.RemoteTestUtils.root;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runner;

public abstract class AbstractRequestTest {

	private static final String paramValue = "blah";
	private static final String paramName = "username";

	private Runner runner;

	String getUri = "/foo";
	String getWithParamsUri = "/foo2";
	String getResponse = "bar";
	String getWithParamResponse = "bar2";
	
	String postUri = "/postUri";
	String postResponse = "postResponses";
	
	String postTestEncodedUri = "/postTestEncodedUri";
	
    @Before
    public void setup() {
        HttpServer server = httpserver(port());
        server.get(by(uri(getUri))).response(getResponse);
        server.get(and(by(uri(getWithParamsUri)), eq(query(paramName), paramValue))).response(getWithParamResponse);
        server.post(and(by(uri(postUri)), eq(form(paramName), paramValue))).response(postResponse);
        
        server.post(and(by(uri(postTestEncodedUri )), eq(form("over"), "WAP"), eq(form("ver"), "2.5"),
        		eq(form("pass"), "zlITAChpmvb25d5GpQuQpA=="), eq(form("mver"), "WAP2.5"), eq(form("ip"), "218.17.157.95"), 
        		eq(form("type"), "2"), eq(form("c_status"), "1"), eq(form("osver"), "WAP"), eq(form("account"), "a2777754"), 
        		eq(form("cid"), "afmobi"), eq(form("token"), "3rnM19Rgv4bF7nmtsl6DYPrc9dy1VrW5kdtp"))).response(postResponse);
        
        server.get(and(by(uri("/redirectGet")), eq(query("redirectParamName"), "redirectParamValue"))).redirectTo(root(port()) + "/redirectedGet?redirectParamName=redirectParamValue");
        server.get(and(by(uri("/redirectedGet")), eq(query("redirectParamName"), "redirectParamValue"))).response("redirectedGetSuccess");
        
        server.post(and(by(uri("/redirectPost")), eq(form("redirectParamName"), "redirectParamValue"))).redirectTo(root(port()) + "/redirectedPost");
        server.post(and(by(uri("/redirectedPost")), eq(form("redirectParamName"), "redirectParamValue"))).response("redirectedPostSuccess");
        
        runner = Runner.runner(server);
        runner.start();
    }

    @After
    public void tearDown() {
        runner.stop();
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map makeParams() {
		Map params = new HashMap<>();
		params.put(paramName, paramValue);
		return params;
	}
	
	public User makeUserBean() {
		User user = new User();
		user.setUsername(paramValue);
		return user;
	}

	
}
