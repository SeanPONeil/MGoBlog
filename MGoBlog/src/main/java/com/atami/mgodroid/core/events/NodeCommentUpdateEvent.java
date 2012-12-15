package com.atami.mgodroid.core.events;


import com.atami.mgodroid.core.NodeComment;
import com.atami.mgodroid.core.NodeIndex;

import java.util.List;

public class NodeCommentUpdateEvent {

    public int nid;
    public List<NodeComment> nodeComments;

    public NodeCommentUpdateEvent(int nid, List<NodeComment> nodeComments){
        this.nid = nid;
        this.nodeComments = nodeComments;
    }
}
