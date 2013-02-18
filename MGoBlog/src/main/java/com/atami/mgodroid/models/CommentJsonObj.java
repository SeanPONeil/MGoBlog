package com.atami.mgodroid.models;


public class CommentJsonObj {

    private String subject;

    private String comment;

    private String uid;

    private String pid;

    private String nid;

    public CommentJsonObj(String subject, String comment, String uid, String pid, String nid) {
        this.subject = subject;
        this.comment = comment;
        this.uid = uid;
        this.pid = pid;
        this.nid = nid;
    }
}
