package com.atami.mgodroid.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

@Table(name = "node_comments")
public class NodeComment extends Model {

    @Column(name = "cid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int cid;

    @Column(name = "pid")
    private int pid;

    @Column(name = "nid")
    private int nid;

    @Column(name = "uid")
    private int uid;

    @Column(name = "subject")
    private String subject;

    @Column(name = "comment")
    private String comment;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "status")
    private int status;

    @Column(name = "format")
    private int format;

    @Column(name = "thread")
    private String thread;

    @Column(name = "name")
    private String name;

    @Column(name = "mail")
    private String mail;

    @Column(name = "homepage")
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
