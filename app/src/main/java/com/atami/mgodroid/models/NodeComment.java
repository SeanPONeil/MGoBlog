package com.atami.mgodroid.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

@Table(name = "node_comments")
public class NodeComment extends Model {

    public NodeComment() {
        super();
    }

    @Expose @Column(name = "cid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int cid;

    @Expose @Column(name = "pid")
    private int pid;

    @Expose @Column(name = "nid")
    private int nid;

    @Expose @Column(name = "uid")
    private int uid;

    @Expose @Column(name = "subject")
    private String subject;

    @Expose @Column(name = "comment")
    private String comment;

    @Expose @Column(name = "hostname")
    private String hostname;

    @Expose @Column(name = "timestamp")
    private long timestamp;

    @Expose @Column(name = "status")
    private int status;

    @Expose @Column(name = "format")
    private int format;

    @Expose @Column(name = "thread")
    private String thread;

    @Expose @Column(name = "name")
    private String name;

    @Expose @Column(name = "mail")
    private String mail;

    @Expose @Column(name = "homepage")
    private String homepage;

    public String getSubject() {
        return subject;
    }

    public String getTimestamp() {
        PrettyTime p = new PrettyTime();
        return p.format(new DateTime(timestamp * 1000).toDate());
    }

    public String getComment() {
        return comment;
    }

    public String getName(){
        return name;
    }

    public int getCommentDepth() {
        return thread.length() - thread.replace(".", "").length();
    }
    
    public int getCid(){
    	return cid;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
