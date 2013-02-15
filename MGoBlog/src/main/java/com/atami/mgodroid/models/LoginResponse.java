package com.atami.mgodroid.models;


public class LoginResponse {

    public String sessId;
    public String session_name;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "sessId='" + sessId + '\'' +
                ", session_name='" + session_name + '\'' +
                '}';
    }
}
