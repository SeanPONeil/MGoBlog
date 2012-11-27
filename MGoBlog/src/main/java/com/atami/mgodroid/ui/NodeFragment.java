package com.atami.mgodroid.ui;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
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

    private Menu nodeMenu;

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
        getWebView().getSettings().setDefaultFontSize(16);
        getWebView().getSettings().setPluginState(WebSettings.PluginState.ON);

        Node node = cache.getNodes().get(nid);
        if (node != null) {
            getWebView().loadDataWithBaseURL("file:///android_asset/", node.getBody(), "text/html", "UTF-8", null);
            getWebView().setVisibility(View.VISIBLE);
            getSherlockActivity().getSupportActionBar().setTitle(node.getTitle());
            getSherlockActivity().getSupportActionBar().setSubtitle(node.getCommentCount() + " comments");
        }else{
            getWebView().setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onNodeUpdate(NodeUpdateEvent event) {
        getWebView().loadDataWithBaseURL("file:///android_asset/", event.node.getBody(), "text/html", "UTF-8", null);
        getWebView().setVisibility(View.VISIBLE);
        getSherlockActivity().getSupportActionBar().setTitle(event.node.getTitle());
        getSherlockActivity().getSupportActionBar().setSubtitle(event.node.getCommentCount() + " comments");
        setRefreshActionItemState(false);
    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (nodeMenu == null) {
            return;
        }
        final MenuItem refreshItem = nodeMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                        .LAYOUT_INFLATER_SERVICE);
                View refreshView = inflater.inflate(R.layout.refresh_menu_item, null);
                refreshItem.setActionView(refreshView);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.node_body, menu);
        nodeMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                setRefreshActionItemState(true);
                cache.refreshNode(nid);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
