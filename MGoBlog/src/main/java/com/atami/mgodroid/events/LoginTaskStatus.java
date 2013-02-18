package com.atami.mgodroid.events;


public class LoginTaskStatus {

    public boolean completed;
    public boolean running;
    public String tag;

    public LoginTaskStatus(boolean completed, boolean running, String tag) {
        this.completed = completed;
        this.running = running;
        this.tag = tag;
    }
}
