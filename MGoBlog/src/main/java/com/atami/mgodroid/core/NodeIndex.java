package com.atami.mgodroid.core;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import static com.activeandroid.util.SQLiteUtils.SQLiteType;

@Table(name = "NodeIndexes")
public class NodeIndex extends Model {

    //ActiveAndroid queries
    public static List<NodeIndex> getAll(String type) {
        return new Select()
                .from(NodeIndex.class)
                .where("type = ?", type)
                .orderBy("created DESC")
                .execute();
    }

    public static void deleteAll(String type) {
        new Delete()
                .from(NodeIndex.class)
                .where("type = ?", type)
                .execute();
    }

    @Column(name = "nid")
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

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getChanged() {
        return changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getPromote() {
        return promote;
    }

    public void setPromote(int promote) {
        this.promote = promote;
    }

    public int getModerate() {
        return moderate;
    }

    public void setModerate(int moderate) {
        this.moderate = moderate;
    }

    public int getSticky() {
        return sticky;
    }

    public void setSticky(int sticky) {
        this.sticky = sticky;
    }

    public int getTnid() {
        return tnid;
    }

    public void setTnid(int tnid) {
        this.tnid = tnid;
    }

    public int getTranslate() {
        return translate;
    }

    public void setTranslate(int translate) {
        this.translate = translate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
