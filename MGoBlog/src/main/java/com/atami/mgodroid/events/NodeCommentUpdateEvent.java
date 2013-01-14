package com.atami.mgodroid.events;


import com.atami.mgodroid.models.NodeComment;

import java.util.List;

public class NodeCommentUpdateEvent {

    public int nid;
    public List<NodeComment> nodeComments;

    public NodeCommentUpdateEvent(int nid, List<NodeComment> nodeComments){
        this.nid = nid;
        this.nodeComments = nodeComments;
    }
}
