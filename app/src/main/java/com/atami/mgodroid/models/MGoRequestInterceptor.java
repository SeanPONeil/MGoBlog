package com.atami.mgodroid.models;

import retrofit.RequestInterceptor;

import javax.inject.Singleton;

@Singleton
public class MGoRequestInterceptor implements RequestInterceptor {

    private String sessid;
    private String session_name;
    private User user;

    public User getUser() {
        return user;
    }

    public void setInterceptor(MGoRequestInterceptor MGoRequestInterceptor){
        this.sessid = MGoRequestInterceptor.sessid;
        this.session_name = MGoRequestInterceptor.session_name;
        this.user = MGoRequestInterceptor.user;
    }

  @Override public void intercept(RequestFacade request) {
    request.addHeader("Accept-Charset", "UTF-8");
    request.addHeader("Cache-Control", "no-cache");
    request.addHeader("Cookie", session_name + "=" + sessid);
  }
}
