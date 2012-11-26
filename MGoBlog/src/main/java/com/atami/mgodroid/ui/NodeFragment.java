package com.atami.mgodroid.ui;

import android.os.Bundle;
import com.atami.mgodroid.core.MGoBlogAPIModule;
import com.atami.mgodroid.core.Node;
import com.atami.mgodroid.ui.base.WebViewFragment;

import javax.inject.Inject;

public class NodeFragment extends WebViewFragment {

    @Inject
    MGoBlogAPIModule.MGoBlogAPI api;

    // ID of the current node
    int nid;

    Node node;

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
        nid = getArguments().getInt("nid");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setDefaultFontSize(14);
    }
}
