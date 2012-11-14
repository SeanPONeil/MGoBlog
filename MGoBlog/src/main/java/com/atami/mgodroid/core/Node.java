package com.atami.mgodroid.core;

import java.util.List;
import java.util.Map;

public class Node {

    public class Taxonomy{
        public int tid;
        public int vid;
        public String name;
        public String description;
        public int weight;
        public int v_weight_unused;
    }

    private int nid;
    private String type;
    private String language;
    private int uid;
    private int status;
    private int created;
    private int changed;
    private int comment;
    private int promote;
    private int moderate;
    private int sticky;
    private int tnid;
    private int translate;
    private int vid;
    private int revision_uid;
    private String title;
    private String body;
    private String teaser;
    private String log;
    private int revision_timestamp;
    private int format;
    private String name;
    private String picture;
    private String data;
    private String path;
    private int last_comment_timestamp;
    private String last_comment_name;
    private int comment_count;
    private Map<Integer, Taxonomy> taxonomy;
    private String[] files;
    private String page_title;
    private String forum_tid;
    private String[] nodewords;
    private String uri;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
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

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
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

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getRevision_uid() {
        return revision_uid;
    }

    public void setRevision_uid(int revision_uid) {
        this.revision_uid = revision_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getRevision_timestamp() {
        return revision_timestamp;
    }

    public void setRevision_timestamp(int revision_timestamp) {
        this.revision_timestamp = revision_timestamp;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLast_comment_timestamp() {
        return last_comment_timestamp;
    }

    public void setLast_comment_timestamp(int last_comment_timestamp) {
        this.last_comment_timestamp = last_comment_timestamp;
    }

    public String getLast_comment_name() {
        return last_comment_name;
    }

    public void setLast_comment_name(String last_comment_name) {
        this.last_comment_name = last_comment_name;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public Map<Integer, Taxonomy> getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Map<Integer, Taxonomy> taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getForum_tid() {
        return forum_tid;
    }

    public void setForum_tid(String forum_tid) {
        this.forum_tid = forum_tid;
    }

    public String[] getNodewords() {
        return nodewords;
    }

    public void setNodewords(String[] nodewords) {
        this.nodewords = nodewords;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
