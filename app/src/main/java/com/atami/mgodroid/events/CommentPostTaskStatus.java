package com.atami.mgodroid.events;


public class CommentPostTaskStatus {

    public boolean completed;
    public boolean running;
    public String tag;

    public CommentPostTaskStatus(boolean completed, boolean running, String tag) {
        this.completed = completed;
        this.running = running;
        this.tag = tag;
    }
}
