package com.atami.mgodroid.events;


public class LoginTaskStatus {

    public boolean running;
    public String tag;

    public LoginTaskStatus(boolean running, String tag) {
        this.running = running;
        this.tag = tag;
    }
}
