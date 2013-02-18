package com.atami.mgodroid.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "sessions")
public class Session extends Model {

    @Column(name = "sessid")
    private String sessid;

    @Column(name = "session_name")
    private String session_name;

    @Column(name = "user")
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

    public String getSessid() {
        return sessid;
    }

    public String getSessionName(){
        return session_name;
    }
}
