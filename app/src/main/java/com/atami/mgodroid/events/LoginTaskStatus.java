package com.atami.mgodroid.events;


public class LoginTaskStatus {

    public boolean success;
    public boolean failure;
    public boolean running;
    public String tag;

    public LoginTaskStatus(boolean success, boolean failure, boolean running, String tag) {
        this.success = success;
        this.failure = failure;
        this.running = running;
        this.tag = tag;
    }
}
