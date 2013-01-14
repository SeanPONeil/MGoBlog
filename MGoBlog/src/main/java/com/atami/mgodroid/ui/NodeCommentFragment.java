package com.atami.mgodroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.events.NodeCommentRefreshEvent;
import com.atami.mgodroid.events.NodeCommentStatusEvent;
import com.atami.mgodroid.events.NodeCommentUpdateEvent;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.atami.mgodroid.ui.base.BaseListFragment;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import retrofit.http.RestException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeCommentFragment extends BaseListFragment {

    // ID of the current node
    int nid;

    private NodeCommentAdapter mAdapter;

    private Menu nodeCommentMenu;

    public static NodeCommentFragment newInstance(int nid) {
        NodeCommentFragment f = new NodeCommentFragment();

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

        mAdapter = new NodeCommentAdapter(getActivity(), android.R.layout.simple_list_item_2,
                new ArrayList<NodeComment>());
        getListView().setAdapter(mAdapter);
    }

    @Subscribe
    public void onNodeCommentUpdate(NodeCommentUpdateEvent event) {
        mAdapter.setNodeComments(event.nodeComments);
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Subscribe
    public void onNodeCommentsStatusUpdate(NodeCommentStatusEvent event) {
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
        if (nodeCommentMenu == null) {
            return;
        }
        final MenuItem refreshItem = nodeCommentMenu.findItem(R.id.refresh);
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
        nodeCommentMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                setRefreshActionItemState(true);
                bus.post(new NodeCommentRefreshEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class WorkerFragment extends BaseFragment {

        public static final String TAG = NodeCommentFragment.class.getName() + "WorkerFragment";

        @Inject
        MGoBlogAPIModule.MGoBlogAPI api;

        int nid;

        List<NodeComment> nodeComments;

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
            nodeComments = Collections.synchronizedList(new ArrayList<NodeComment>());
            getFromDisk();
            refresh();
        }


        private void getFromDisk() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nodeComments = NodeComment.getAll(nid);
                    bus.post(produceNodeComments());
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
                        nodeComments = api.getNodeComments(nid);
                        NodeComment.deleteAll(nid);
                        bus.post(produceNodeComments());
                        for (NodeComment comment : nodeComments) {
                            comment.save();
                        }
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
        public void onNodeRefresh(NodeCommentRefreshEvent event) {
            if (!refreshing) {
                refresh();
            }
        }

        @Produce
        public NodeCommentUpdateEvent produceNodeComments() {
            return new NodeCommentUpdateEvent(nid, nodeComments);
        }

        @Produce
        public NodeCommentStatusEvent produceStatus() {
            return new NodeCommentStatusEvent(refreshing);
        }
    }
}
