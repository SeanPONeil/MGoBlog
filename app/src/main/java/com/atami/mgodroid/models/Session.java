package com.atami.mgodroid.models;

import retrofit.http.Header;
import retrofit.http.Headers;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class Session implements Headers{

    private String sessid;
    private String session_name;
    private User user;

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessid='" + sessid + '\'' +
                ", session_name='" + session_name + '\'' +
                ", user=" + user +
                '}';
    }

    public void setSession(Session session){
        this.sessid = session.sessid;
        this.session_name = session.session_name;
        this.user = session.user;
    }

    @Override
    public List<Header> get() {
        return Arrays.asList(new Header("Accept-Charset", "UTF-8"),
                new Header("Content-Type", "application/json"),
                new Header("Cache-Control", "no-cache"),
                new Header("Cookie", session_name + "=" + sessid));
    }
}
