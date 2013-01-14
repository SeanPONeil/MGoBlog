package com.atami.mgodroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.atami.mgodroid.models.Node;
import com.atami.mgodroid.events.NodeRefreshEvent;
import com.atami.mgodroid.events.NodeStatusEvent;
import com.atami.mgodroid.events.NodeUpdateEvent;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.atami.mgodroid.ui.base.WebViewFragment;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import retrofit.http.RestException;

import javax.inject.Inject;

public class NodeFragment extends WebViewFragment {

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
        getWebView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Subscribe
    public void onNodeUpdate(NodeUpdateEvent event) {
        if (event.node != null) {
            getWebView().loadDataWithBaseURL("file:///android_asset/", event.node.getBody(), "text/html", "UTF-8", null);
            getSherlockActivity().getSupportActionBar().setTitle(event.node.getTitle());
            getSherlockActivity().getSupportActionBar().setSubtitle("By " + event.node.getName() + " - " + event.node
                    .getCommentCount() + " " + "comments");
        }
    }

    @Subscribe
    public void onNodeStatusUpdate(NodeStatusEvent event) {
        if (event.refreshing) {
            setRefreshActionItemState(true);
        } else {
            setRefreshActionItemState(false);
        }
    }

    @Subscribe
    public void onNetworkError(RestException.NetworkException e) {
        Crouton.makeText(getActivity(), e.getMessage(), Style.INFO).show();
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
                bus.post(new NodeRefreshEvent());
                return true;
            case R.id.comments:
                // Instantiate a new fragment.
                Fragment newFragment = NodeCommentFragment.newInstance(nid);
                Fragment workerFragment = NodeCommentFragment.WorkerFragment.newInstance(nid);

                // Add the fragment to the activity, pushing this transaction
                // on to the back stack.
                FragmentTransaction ft = getSherlockActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                        R.anim.fragment_slide_left_exit,
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_right_exit);
                ft.replace(R.id.fragment_pane, newFragment);
                ft.add(workerFragment, NodeCommentFragment.WorkerFragment.TAG);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class WorkerFragment extends BaseFragment {

        public static final String TAG = NodeFragment.class.getName() + "WorkerFragment";

        @Inject
        MGoBlogAPIModule.MGoBlogAPI api;

        int nid;

        Node node;

        boolean refreshing = false;

        public static WorkerFragment newInstance(int nid) {
            WorkerFragment f = new WorkerFragment();

            Bundle args = new Bundle();
            args.putInt("nid", nid);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            nid = getArguments().getInt("nid");
            setRetainInstance(true);
            node = null;
            getFromDisk();
            refresh();
        }


        private void getFromDisk() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    node = Node.get(nid);
                    bus.post(produceNode());
                }
            }).start();
        }

        private void refresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        refreshing = true;
                        bus.post(produceStatus());
                        Node newNode = api.getNode(nid);
                        if (node != null) {
                            if (newNode.getRevisionTimestamp() == node.getRevisionTimestamp()) {
                                return;
                            }
                            node.delete();
                        }
                        node = newNode;
                        node.clean();
                        bus.post(produceNode());
                        node.save();
                    } catch (RestException.NetworkException e) {
                        e.printStackTrace();
                        bus.post(e);
                    } finally {
                        refreshing = false;
                        bus.post(produceStatus());
                    }
                }
            }).start();
        }

        @Subscribe
        public void onNodeRefresh(NodeRefreshEvent event) {
            if (!refreshing) {
                refresh();
            }
        }

        @Produce
        public NodeUpdateEvent produceNode() {
            return new NodeUpdateEvent(node);
        }

        @Produce
        public NodeStatusEvent produceStatus() {
            return new NodeStatusEvent(refreshing);
        }
    }
}
