package com.atami.mgodroid.models;

import com.google.gson.annotations.Expose;

public class LoginJsonObj {

    @Expose public String username;
    @Expose public String password;

    public LoginJsonObj(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
