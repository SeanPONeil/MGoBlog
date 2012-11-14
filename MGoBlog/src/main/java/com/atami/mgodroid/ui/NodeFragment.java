package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.atami.mgodroid.core.APIModule.MGoBlogAPI;
import com.atami.mgodroid.core.Node;
import com.atami.mgodroid.ui.base.WebViewFragment;
import com.github.kevinsawicki.wishlist.AsyncLoader;

import javax.inject.Inject;

public class NodeFragment extends WebViewFragment implements LoaderManager.LoaderCallbacks<Node> {

    @Inject
    MGoBlogAPI api;

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

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Node> onCreateLoader(int id, Bundle bundle) {
        return new AsyncLoader<Node>(getActivity()) {
            @Override
            public Node loadInBackground() {
                try {
                    return api.getNode(String.valueOf(nid));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Node> loader, Node node) {
        this.node = node;
        getSherlockActivity().getSupportActionBar().setTitle(node.getTitle());
        getSherlockActivity().getSupportActionBar().setSubtitle(String.valueOf(node.getComment_count()) + " comments");
        getWebView().loadDataWithBaseURL("http://mgoblog.com/", node.getBody(), "text/html", "UTF-8", null);
    }

    @Override
    public void onLoaderReset(Loader<Node> loader) {
        node = null;
    }
}
