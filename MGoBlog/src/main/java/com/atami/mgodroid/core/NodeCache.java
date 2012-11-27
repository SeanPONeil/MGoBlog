package com.atami.mgodroid.core;


import android.util.SparseArray;
import com.atami.mgodroid.core.events.NodeUpdateEvent;
import com.squareup.otto.Bus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.atami.mgodroid.core.MGoBlogAPIModule.MGoBlogAPI;

public class NodeCache {

    Bus bus;

    MGoBlogAPI api;

    //Map of Nodes that are hashed by their
    //nid. This ISN'T synchronized to the
    //best of my knowledge.
    //TODO: make synchronized
    private SparseArray<Node> nodes;

    public SparseArray<Node> getNodes() {
        return nodes;
    }

    public NodeCache(Bus bus, MGoBlogAPI api) {
        this.bus = bus;
        this.api = api;
        bus.register(this);
        nodes = new SparseArray<Node>();
        //updateNodesFromDisk();
    }

    public synchronized void refreshNode(final int nid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Node node = api.getNode(nid);

                //Add MGoBlog css before storing
                Document doc = Jsoup.parseBodyFragment(node.getBody());
                Element headNode = doc.head();
                headNode.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"node_body.css\"></style>");

                //Wrap video embeds in divs for CSS purposes
                for(Element embed: doc.select("embed, iframe, object")){
                    embed.wrap("<div class=\"video-container\"></div>");
                }
                node.setBody(doc.toString());


                nodes.put(nid, node);
                bus.post(new NodeUpdateEvent(node));
                //node.save();
            }
        }).start();
    }


}
