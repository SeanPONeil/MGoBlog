package com.atami.mgodroid.models;


import com.google.gson.annotations.Expose;

public class CommentJsonObj {

    @Expose private String subject;

    @Expose private String comment;

    @Expose private String uid;

    @Expose private String pid;

    @Expose private String nid;

    public CommentJsonObj(String subject, String comment, String uid, String pid, String nid) {
        this.subject = subject;
        this.comment = comment;
        this.uid = uid;
        this.pid = pid;
        this.nid = nid;
    }
}
