package com.atami.mgodroid.io;


import com.atami.mgodroid.models.LoginJsonObj;
import com.atami.mgodroid.models.Session;
import com.squareup.tape.Task;

import javax.inject.Inject;

import retrofit.Callback;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

public class LoginTask implements Task<Callback<Session>> {

    public static final String TAG = "LoginTask";

    private String username;
    private String password;
    private String tag;

    @Inject
    MGoBlogAPI api;

    public LoginTask(String username, String password, String tag) {
        this.username = username;
        this.password = password;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void execute(Callback<Session> callback) {
        api.loginUser(new LoginJsonObj(username, password), callback);
    }
}
