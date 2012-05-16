package com.atami.mgodroid.io;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class APITest extends TestCase{
	

	@Test
	public void testConnect() {
		JSONObject json = null;
		try {
			json = new JSONObject(API.connect());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			assertNotNull("Testing System Connect", json.get("sessid"));
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}

}
