package com.atami.mgodroid.models;


import java.util.Map;

public class User {

    private int uid;
    private String name;
    private String mail;
    private int mode;
    private int sort;
    private int threshold;

    //private String theme;
    private String signature;
    private long created;
    private long login;
    private int status;

    //private String language;
    private String picture;
    private String init;
    //private String data;
    //private String timezone_name;
    private int signature_format;

    //private String form_buid_id;
    //private Map<String, String> mollom;
    //private String picture_delete;
    //private String picture_upload;
    //private String block;
    //private String openidurl_server;
    //private String openidurl_delegate;
    //private String openidurl_xrds;
    //private int endure_misery;
    private int cave;
    private String comments_per_page;
    private Map<String, String> roles;

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
