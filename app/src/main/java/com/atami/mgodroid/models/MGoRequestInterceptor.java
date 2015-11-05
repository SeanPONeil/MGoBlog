package com.atami.mgodroid.models;

import com.google.gson.annotations.Expose;

import retrofit.RequestInterceptor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MGoRequestInterceptor implements RequestInterceptor {

    @Expose private String sessid;
    @Expose private String session_name;
    @Expose private User user;

    @Inject public MGoRequestInterceptor() {}

    public User getUser() {
        return user;
    }

    public void setInterceptor(MGoRequestInterceptor mGoRequestInterceptor){
        this.sessid = mGoRequestInterceptor.sessid;
        this.session_name = mGoRequestInterceptor.session_name;
        this.user = mGoRequestInterceptor.user;
    }

  @Override public void intercept(RequestFacade request) {
    request.addHeader("Accept-Charset", "UTF-8");
    request.addHeader("Cache-Control", "no-cache");
    request.addHeader("Cookie", session_name + "=" + sessid);
  }
}
