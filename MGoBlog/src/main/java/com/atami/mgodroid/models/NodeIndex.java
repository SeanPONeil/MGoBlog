package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

@Table(name = "NodeIndexes")
public class NodeIndex extends Model {

    @Column(name = "nid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int nid;

    @Column(name = "vid")
    private int vid;

    @Column(name = "type")
    private String type;

    @Column(name = "language")
    private String language;

    @Column(name = "title")
    private String title;

    @Column(name = "uid")
    private int uid;

    @Column(name = "status")
    private int status;

    @Column(name = "created")
    private long created;

    @Column(name = "changed")
    private long changed;

    @Column(name = "comment")
    private int comment;

    @Column(name = "promote")
    private int promote;

    @Column(name = "moderate")
    private int moderate;

    @Column(name = "sticky")
    private int sticky;

    @Column(name = "tnid")
    private int tnid;

    @Column(name = "translate")
    private int translate;

    @Column(name = "uri")
    private String uri;

    //ActiveAndroid queries
    public static From selectFromDBWhere(String where){
         return new Select().from(NodeIndex.class).where(where).orderBy("created DESC");
    }

    public static From deleteFromDBWhere(String where){
        return new Delete().from(NodeIndex.class).where(where).orderBy("created DESC");
    }

    public int getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public long getCreated() {
        return created;
    }
}
