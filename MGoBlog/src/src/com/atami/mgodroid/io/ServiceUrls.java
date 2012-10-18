package com.atami.mgodroid.io;

public class ServiceUrls {

	static final String USERNAME_KEY = "username";
	static final String PASSWORD_KEY = "password";

	static final String BASE_URL = "http://mgoblog.com/mobileservices/";
	
	static final String NODE_GET_URL = BASE_URL + "node/%s.json";
	
	static final String NODE_COMMENTS_GET_URL = BASE_URL
			+ "node/%s/comments.json";
	
	static final String USER_LOGIN_URL = BASE_URL + "user/login";
	
	static final String NODE_COMMENT_SAVE_URL = BASE_URL + "comment.json";

	static final String SYSTEM_CONNECT_URL = BASE_URL + "system/connect/";

	static final String NODE_INDEX_URL = BASE_URL
			+ "node.json?%s&%s&page=%s";
	
	static final String MGOBOARD_INDEX = BASE_URL + "node.json?parameters[type]=forum&page=%s";
	
	static final String MGOBLOG_INDEX = BASE_URL + "node.json?parameters[promoted]=1&page=%s";
	
	static final String DIARIES_INDEX = BASE_URL + "node.json?parameters[type]=blog&page=%s";
	
	static final String MGOLICIOUS_INDEX = BASE_URL + "node.json?parameters[type]=link&page=%s";

}
