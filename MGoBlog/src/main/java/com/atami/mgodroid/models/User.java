package com.atami.mgodroid.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Map;

@Table(name = "users")
public class User extends Model {

    @Column(name = "uid")
    private int uid;

    @Column(name = "name")
    private String name;

    @Column(name = "mail")
    private String mail;

    @Column(name = "mode")
    private int mode;

    @Column(name = "sort")
    private int sort;

    @Column(name = "threshold")
    private int threshold;

    //private String theme;

    @Column(name = "signature")
    private String signature;

    @Column(name = "created")
    private long created;

    @Column(name = "login")
    private long login;

    @Column(name = "status")
    private int status;

    //private String language;

    @Column(name = "picture")
    private String picture;

    @Column(name = "init")
    private String init;
    //private String data;
    //private String timezone_name;

    @Column(name = "signature_format")
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

    @Column(name = "cave")
    private int cave;

    @Column(name = "comments_per_page")
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

    public String getName(){
        return name;
    }

    public Map<String, String> getRoles(){
        return roles;
    }
}
