package com.atami.mgodroid.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.common.io.CharStreams;

public class API {

	public static final String TAG = "API";

	// // Pulls in a fresh set of Node Resource data from MGoBlog based on the
	// // position in the ViewFlow.
	// public static JSONArray getNodeResource(int position, int page,
	// Context context) throws MalformedURLException, IOException,
	// JSONException {
	//
	// String url = String.format(ServiceUrls.NODE_INDEX_TYPE,
	// ServiceUrls.NODE_INDEX_TYPE[position],
	// Integer.toString(page));
	//
	// HttpURLConnection request = (HttpURLConnection) new URL(url)
	// .openConnection();
	// request.setRequestProperty("Accept-Charset", ENCODING);
	//
	// // Guava library magic to stringify input stream data
	// String result = CharStreams.toString(new InputStreamReader(request
	// .getInputStream(), ENCODING));
	// Log.v(TAG, request.getResponseMessage());
	//
	// JSONArray json = new JSONArray(result);
	// ArrayList<DrupalNode> nodes = new ArrayList<DrupalNode>();
	// Gson gson = new Gson();
	// Type mapType = new TypeToken<Map<String, String>>() {
	// }.getType();
	// for (int i = 0; i < 20; i++) {
	// HashMap<String, String> obj = gson.fromJson(json.getString(i),
	// mapType);
	// nodes.add(new DrupalNode(obj));
	// }
	// return nodes;
	// }
	//
	// public static ArrayList<DrupalNode> getComments(String nid,
	// String commentCount, Context context) throws MalformedURLException,
	// IOException, JSONException {
	//
	// String url = String.format(NODE_COMMENTS_GET, nid);
	//
	// HttpURLConnection request = (HttpURLConnection) new URL(url)
	// .openConnection();
	// request.setRequestProperty("Accept-Charset", ENCODING);
	//
	// String result = CharStreams.toString(new InputStreamReader(request
	// .getInputStream(), ENCODING));
	//
	// JSONArray json = new JSONArray(result);
	// ArrayList<DrupalNode> nodes = new ArrayList<DrupalNode>();
	// Gson gson = new Gson();
	// Type mapType = new TypeToken<Map<String, String>>() {
	// }.getType();
	// for (int i = 0; i < Integer.parseInt(commentCount); i++) {
	// HashMap<String, String> obj = gson.fromJson(json.getString(i),
	// mapType);
	// nodes.add(new DrupalNode(obj));
	// }
	// return nodes;
	//
	// }
	//
	// public static boolean userLogin(String username, String password,
	// Context context) throws JSONException, MalformedURLException,
	// IOException {
	//
	// DataOutputStream outStream;
	// DataInputStream inStream;
	// HttpURLConnection request;
	// JSONObject data;
	//
	// // Construct parameters as JSON Object
	// data = new JSONObject();
	// data.put(USERNAME, username);
	// data.put(PASSWORD, password);
	// Log.d(TAG, data.toString());
	//
	// // Create POST connection to URL
	// request = (HttpURLConnection) new URL(USER_LOGIN).openConnection();
	// request.setDoOutput(true);
	// request.setRequestMethod("POST");
	// request.setRequestProperty("Accept-Charset", ENCODING);
	// request.setRequestProperty("Content-Type", APPLICATION_JSON);
	//
	// // Open output stream and send POST request
	// outStream = new DataOutputStream(request.getOutputStream());
	// Log.v(TAG, "Begin POST Request");
	// outStream.writeBytes(data.toString());
	// outStream.flush();
	// outStream.close();
	// Log.v(TAG, request.getResponseMessage());
	//
	// // Open input stream
	// inStream = new DataInputStream(request.getInputStream());
	// String result = CharStreams.toString(new InputStreamReader(inStream,
	// ENCODING));
	// JSONObject json = new JSONObject(result);
	// if (json.get("sessid") == null) {
	// return false;
	// } else {
	// ((Globals) context.getApplicationContext())
	// .setUID((String) ((JSONObject) json.get("user")).get("uid"));
	// Log.v(TAG, json.toString());
	//
	// Log.v(TAG,
	// json.getString("session_name") + "="
	// + json.getString("sessid"));
	// ((Globals) context.getApplicationContext())
	// .setSessionID(json.getString("session_name") + "="
	// + json.getString("sessid"));
	// return true;
	// }
	// }
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

	private static String post(String url) throws MalformedURLException,
			IOException {

		HttpURLConnection conn;
		conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "application/json");

		return CharStreams.toString(new InputStreamReader(
				conn.getInputStream(), "UTF=8"));
	}

	// Returns a SessID on successful connect
	public static String connect() throws MalformedURLException, IOException,
			JSONException {

		String result = post(ServiceUrls.SYSTEM_CONNECT_URL);

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
