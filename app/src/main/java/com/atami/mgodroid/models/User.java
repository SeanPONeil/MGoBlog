package com.atami.mgodroid.models;


import com.google.gson.annotations.Expose;

import java.util.Map;

public class User {

    @Expose private int uid;
    @Expose private String name;
    @Expose private String mail;
    @Expose private int mode;
    @Expose private int sort;
    @Expose private int threshold;

    //@Expose private String theme;
    @Expose private String signature;
    @Expose private long created;
    @Expose private long login;
    @Expose private int status;

    //@Expose private String language;
    @Expose private String picture;
    @Expose private String init;
    //@Expose private String data;
    //@Expose private String timezone_name;
    @Expose private int signature_format;

    //@Expose private String form_buid_id;
    //@Expose private Map<String, String> mollom;
    //@Expose private String picture_delete;
    //@Expose private String picture_upload;
    //@Expose private String block;
    //@Expose private String openidurl_server;
    //@Expose private String openidurl_delegate;
    //@Expose private String openidurl_xrds;
    //@Expose private int endure_misery;
    @Expose private int cave;
    @Expose private String comments_per_page;
    @Expose private Map<String, String> roles;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", mode=" + mode +
                ", sort=" + sort +
                ", threshold=" + threshold +
                ", signature='" + signature + '\'' +
                ", created=" + created +
                ", login=" + login +
                ", status=" + status +
                ", picture='" + picture + '\'' +
                ", init='" + init + '\'' +
                ", signature_format=" + signature_format +
                ", cave=" + cave +
                ", comments_per_page='" + comments_per_page + '\'' +
                ", roles=" + roles +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getRoles() {
        return roles;
    }
}
