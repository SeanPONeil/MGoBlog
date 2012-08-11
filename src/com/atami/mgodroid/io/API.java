package com.atami.mgodroid.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Static methods for interacting with the MGoBlog API. All functions in this
 * class rely on network I/O, and because of this need to be called off of the
 * main thread.
 * 
 * @author Sean
 */
public class API {

	public static class NodeIndexType {
		public static final int MGOBOARD = 0;
		public static final int MGOBLOG = 1;
		public static final int DIARIES = 2;
		public static final int MGOLICIOUS = 3;
	}

	private static final String[] NODE_INDEX_PARAMS = {
			"parameters[type]=forum", "parameters[promote]=1",
			"parameters[type]=blog", "parameters[type]=link" };

	private static final String STICKY_PARAM = "parameters[sticky]=%s";

	/**
	 * Retrieves a set of node indices.
	 * 
	 * @param type
	 *            The type of node index to retrieve. See API.NodeIndexType
	 * @param page
	 *            The page to retrieve. Pages are 0 indexed.
	 * @param getStickies
	 *            Boolean value that determines whether or not stickies are
	 *            retrieved.
	 * @return JSONArray of node index JSONObjects
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray getNodeIndex(int type, int page, boolean getStickies)
			throws MalformedURLException, IOException, JSONException {

		String url = String.format(ServiceUrls.NODE_INDEX_URL,
				NODE_INDEX_PARAMS[type],
				String.format(STICKY_PARAM, getStickies ? 1 : 0),
				Integer.toString(page));

		String result = get(url);
		return new JSONArray(result);
	}

	public static JSONArray getComments(String nid, String commentCount,
			Context context) throws MalformedURLException, IOException,
			JSONException {
		String url = String.format(ServiceUrls.NODE_COMMENTS_GET_URL, nid);
		String result = get(url);
		return new JSONArray(result);
	}

	public static JSONObject userLogin(String username, String password,
			Context context) throws JSONException, MalformedURLException,
			IOException {

		// POST request body
		JSONObject payload = new JSONObject();
		payload.put("username", username);
		payload.put("password", password);
		String result = post(ServiceUrls.USER_LOGIN_URL, payload, null);
		return new JSONObject(result);
	}

	public static JSONObject getNode(int nid) throws MalformedURLException,
			IOException, JSONException {

		String url = String.format(ServiceUrls.NODE_GET_URL, nid);
		String result = get(url);
		return new JSONObject(result);
	}

	//
	// public static boolean postComment(Context context, String subject,
	// String nid, String body, String pid) throws Exception {
	//
	// // Get UID from global, or login and get new UID
	// String uid = ((Globals) context.getApplicationContext()).getUID();
	// String sessID = ((Globals) context.getApplicationContext())
	// .getSessionID();
	// if (uid == null) {
	// SharedPreferences settings = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// userLogin(settings.getString(USERNAME, null),
	// settings.getString(PASSWORD, null), context);
	// uid = ((Globals) context.getApplicationContext()).getUID();
	// sessID = ((Globals) context.getApplicationContext()).getSessionID();
	// }
	//
	// // Construct commentJSON Objects
	// JSONObject data = new JSONObject();
	// data.put("subject", subject);
	// data.put("comment", body);
	// data.put("uid", uid);
	// data.put("nid", nid);
	// data.put("pid", pid);
	// Log.v(TAG, data.toString());
	//
	// // Open POST connection
	// HttpURLConnection request;
	// request = (HttpURLConnection) new URL(NODE_COMMENT_SAVE)
	// .openConnection();
	// request.setDoOutput(true);
	// request.setRequestMethod("POST");
	// request.setRequestProperty("Accept-Charset", ENCODING);
	// request.setRequestProperty("Content-Type", APPLICATION_JSON);
	// request.setRequestProperty("Cookie", sessID);
	//
	// // Open output stream and send POST request
	// DataOutputStream outStream = new DataOutputStream(
	// request.getOutputStream());
	// Log.v(TAG, "Begin POST Request");
	// outStream.writeBytes(data.toString());
	// outStream.flush();
	// outStream.close();
	// Log.v(TAG, request.getResponseMessage());
	//
	// if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
	// DataInputStream inStream = new DataInputStream(request.getInputStream());
	// String result = CharStreams.toString(new InputStreamReader(
	// inStream, ENCODING));
	// Log.v(TAG, result);
	// return true;
	// } else {
	// Log.v(TAG, request.getResponseMessage());
	// return false;
	// }
	//
	// }

	/**
	 * Execute a GET request
	 * 
	 * @param url
	 *            the URL to retrieve
	 * @return content provided by URL as a String
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static String get(String url) throws MalformedURLException,
			IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestProperty("Accept-Charset", "UTF-8");

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream is = conn.getInputStream();
			return convertStreamToString(is);
		} else {
			throw new IOException();
		}
	}

	/**
	 * Execute a POST request
	 * 
	 * @param url
	 *            the URL to post to
	 * @param payload
	 *            the JSONObject to post
	 * @param cookie
	 *            optional cookie data to send with request
	 * @return content provided by URL as a String
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static String post(String url, JSONObject payload, String cookie)
			throws MalformedURLException, IOException {

		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "application/json");
		if (cookie != null) {
			conn.setRequestProperty("Cookie", cookie);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.writeBytes(payload.toString());
		outStream.flush();
		outStream.close();

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream is = conn.getInputStream();
			return convertStreamToString(is);
		} else {
			throw new IOException();
		}
	}

	/**
	 * Helper method for converting an InputStream into it's String
	 * representation.
	 * 
	 * @param any
	 *            InputStream is
	 * @return String version
	 */
	private static String convertStreamToString(InputStream is) {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
}
