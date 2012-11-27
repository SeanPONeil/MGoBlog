package com.atami.mgodroid.ui;

import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.Node;
import com.atami.mgodroid.core.NodeCache;
import com.atami.mgodroid.core.events.NodeUpdateEvent;
import com.atami.mgodroid.ui.base.WebViewFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class NodeFragment extends WebViewFragment {

    @Inject
    NodeCache cache;

    // ID of the current node
    int nid;

    public static NodeFragment newInstance(int nid) {
        NodeFragment f = new NodeFragment();

        Bundle args = new Bundle();
        args.putInt("nid", nid);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        nid = getArguments().getInt("nid");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setDefaultFontSize(14);

        Node node = cache.getNodes().get(nid);
        if (node != null) {
            getWebView().loadDataWithBaseURL(null, node.getBody(), "text/html", "UTF-8", null);
        }
    }

    @Subscribe
    public void onNodeUpdate(NodeUpdateEvent event) {
        getWebView().loadDataWithBaseURL(null, event.node.getBody(), "text/html", "UTF-8", null);
        //stop refreshing
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.node_body, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                cache.refreshNode(nid);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
