package com.atami.mgodroid.io;


import com.atami.mgodroid.models.CommentJsonObj;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.squareup.tape.Task;

import javax.inject.Inject;

import retrofit.Callback;

public class CommentPostTask implements Task<Callback> {

    public final static String TAG = "CommentPostTask";
    @Inject
    MGoBlogAPIModule.MGoBlogAPI api;
    CommentJsonObj payload;
    int uid;
    String tag;

    public CommentPostTask(CommentJsonObj payload, int uid, String tag) {
        this.payload = payload;
        this.uid = uid;
        this.tag = tag;
    }

    @Override
    public void execute(final Callback callback) {
        api.postComment(payload, callback);
    }

    public String getTag() {
        return tag;
    }
}
