package com.atami.mgodroid.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class API {

	public static final String TAG = "API";

	// Pulls in a fresh set of Node Index data from MGoBlog based on the
	// position in the ViewFlow.
	public static JSONArray getNodeIndex(String type, int page, Context context)
			throws MalformedURLException, IOException, JSONException {
		String url = String.format(ServiceUrls.NODE_INDEX_URL, type,
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

	public static boolean userLogin(String username, String password,
			Context context) throws JSONException, MalformedURLException,
			IOException {

		//POST request body
		JSONObject payload = new JSONObject();
		payload.put("username", username);
		payload.put("password", password);
		Log.d(TAG, payload.toString());
		
		String result = post(ServiceUrls.USER_LOGIN_URL, payload, null);
		
		JSONObject json = new JSONObject(result);
		if (json.get("sessid") == null) {
			return false;
		} else {
			//Do something with sessid here
			return true;
		}
	}

	//
	// public static DrupalNode getNode(String nid, Context context)
	// throws Exception {
	//
	// URLConnection conn = new URL(String.format(NODE_GET, nid))
	// .openConnection();
	// conn.setRequestProperty("Accept-Charset", ENCODING);
	// InputStream response = conn.getInputStream();
	// String result = CharStreams.toString(new InputStreamReader(response,
	// ENCODING));
	// Log.v(TAG, result);
	// JSONObject json = new JSONObject(result);
	// json.remove("taxonomy");
	// json.remove("files");
	// json.remove("nodewords");
	// json.remove("page_title");
	//
	// Gson gson = new Gson();
	// Type mapType = new TypeToken<Map<String, String>>() {
	// }.getType();
	//
	// Log.v(TAG, json.toString());
	//
	// HashMap<String, String> obj = gson.fromJson(json.toString(), mapType);
	// return new DrupalNode(obj);
	// }
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

	private static String get(String url) throws MalformedURLException,
			IOException {
		HttpURLConnection conn;
		conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("Accept-Charset", "UTF-8");

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return CharStreams.toString(new InputStreamReader(conn
					.getInputStream(), "UTF-8"));
		} else {
			return null;
		}

	}

	private static String post(String url, JSONObject payload, String cookie)
			throws MalformedURLException, IOException {

		HttpURLConnection conn;
		conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "application/json");
		if (cookie != null) {
			conn.setRequestProperty("Cookie", cookie);
		}

		// Open output stream and send POST request
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		Log.v(TAG, "Begin POST Request");
		Log.v(TAG, "POST request body: " + payload.toString());
		outStream.writeBytes(payload.toString());
		outStream.flush();
		outStream.close();
		Log.v(TAG, "POST Response: " + conn.getResponseMessage());

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return CharStreams.toString(new InputStreamReader(conn
					.getInputStream(), "UTF-8"));
		} else {
			return null;
		}

	}

	// Returns a SessID on successful connect
	public static String connect() throws MalformedURLException, IOException,
			JSONException {

		String result = post(ServiceUrls.SYSTEM_CONNECT_URL, null, null);

		JSONObject json = new JSONObject(result);
		return json.toString();
		// Log.v(TAG, json.toString());
		// if (json.get("sessid") == null) {
		// return null;
		// } else {
		// return (String) json.get("sessid");
		// }
	}

}
