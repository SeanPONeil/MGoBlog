package com.atami.mgodroid.io;


import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.squareup.tape.Task;
import retrofit.http.Callback;

import javax.inject.Inject;

import java.util.List;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

public class NodeCommentTask implements Task<Callback<List<NodeComment>>> {

    public static final String TAG = "NodeCommentTask";

    private int nid;
    private String tag;

    @Inject
    MGoBlogAPI api;

    /**
     * Downloads the comments for a Node from MGoBlog
     * @param nid the nid of the Node to retrieve comments for
     * @param tag identifier for this Task
     */
    public NodeCommentTask(int nid, String tag){
        this.nid = nid;
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }


    @Override
    public void execute(Callback<List<NodeComment>> callback) {
        api.getNodeComments(nid, callback);
    }
}
