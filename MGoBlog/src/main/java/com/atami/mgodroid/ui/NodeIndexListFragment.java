package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.MGoBlogAPIModule;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.core.events.NodeIndexNextPageEvent;
import com.atami.mgodroid.core.events.NodeIndexRefreshEvent;
import com.atami.mgodroid.core.events.NodeIndexStatusEvent;
import com.atami.mgodroid.core.events.NodeIndexUpdateEvent;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import retrofit.http.RestException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static retrofit.http.RestException.NetworkException;

public class NodeIndexListFragment extends PullToRefreshListFragment
        implements OnLastItemVisibleListener, OnRefreshListener<ListView> {

    //Type parameter used in API call
    private String type;

    private String title;

    // This is the Adapter being used to display the list's data.
    private NodeIndexAdapter mAdapter;

    public static NodeIndexListFragment newInstance(String title, String type) {
        NodeIndexListFragment f = new NodeIndexListFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("type", type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        title = getArguments().getString("title");
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            bus.post(new NodeIndexRefreshEvent(type));
        }

        getSherlockActivity().getSupportActionBar().setTitle(title);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        mAdapter = new NodeIndexAdapter(getActivity(), R.layout.list_item_two_line, new ArrayList<NodeIndex>());
        setListAdapter(mAdapter);

        getPullToRefreshListView().setOnLastItemVisibleListener(this);
        getPullToRefreshListView().setOnRefreshListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        bus.post(mAdapter.getItem(position - 1));
    }

    @Override
    public void onLastItemVisible() {
        bus.post(new NodeIndexNextPageEvent(type));
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        getPullToRefreshListView().setLastUpdatedLabel(
                DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));
        bus.post(new NodeIndexRefreshEvent(type));
    }

    @Subscribe
    public void onNodeIndexUpdate(NodeIndexUpdateEvent event) {
        List<NodeIndex> list = event.nodeIndexes;
        mAdapter.setNodeIndexes(list);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onNetworkError(NetworkException e) {
        Crouton.makeText(getActivity(), e.getMessage(), Style.INFO).show();
        getPullToRefreshListView().onRefreshComplete();
    }

    @Subscribe
    public void onRefreshEvent(NodeIndexRefreshEvent event) {
        if (type.equals(event.type)) {
            getPullToRefreshListView().setRefreshing();
        }
    }

    @Subscribe
    public void onNodeIndexStatusEvent(NodeIndexStatusEvent event) {
        if (event.refreshing) {
            getPullToRefreshListView().setRefreshing();
        } else {
            getPullToRefreshListView().onRefreshComplete();
        }
        if (event.gettingNextPage) {
            //set refreshing footer
        } else {
            //unset refreshing footer
        }
    }

    public static class WorkerFragment extends BaseFragment {

        public static final String TAG = NodeIndexListFragment.class.getName() + "WorkerFragment";

        @Inject
        MGoBlogAPIModule.MGoBlogAPI api;

        String type;

        List<NodeIndex> nodeIndexes;

        boolean refreshing = false;
        boolean gettingNextPage = false;

        public static WorkerFragment newInstance(String type) {
            WorkerFragment f = new WorkerFragment();

            Bundle args = new Bundle();
            args.putString("type", type);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            type = getArguments().getString("type");
            setRetainInstance(true);
            nodeIndexes = Collections.synchronizedList(new ArrayList<NodeIndex>());
            getFromDisk();
        }

        private void getFromDisk() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nodeIndexes = NodeIndex.getAll(type);
                    bus.post(produceNodeIndexes());
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
                        List<NodeIndex> list = api.getNodeIndex(type, "0");
                        NodeIndex.deleteAll(type);
                        nodeIndexes = list;
                        bus.post(produceNodeIndexes());
                        for (NodeIndex nodeIndex : list) {
                            nodeIndex.save();
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

        private void getNextPage() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        gettingNextPage = true;
                        bus.post(produceStatus());
                        List<NodeIndex> list = api.getNodeIndex(type, String.valueOf(nodeIndexes.size() / 20));
                        nodeIndexes.addAll(list);
                        bus.post(produceNodeIndexes());
                        for (NodeIndex nodeIndex : list) {
                            nodeIndex.save();
                        }
                    } catch (RestException.NetworkException e) {
                        e.printStackTrace();
                        bus.post(e);
                    } finally {
                        gettingNextPage = false;
                        bus.post(produceNodeIndexes());
                    }

                }
            }).start();
        }

        @Subscribe
        public void onNodeIndexRefresh(NodeIndexRefreshEvent event) {
            if (!refreshing) {
                refresh();
            }
        }

        @Subscribe
        public void onNodeIndexNextPageEvent(NodeIndexNextPageEvent event) {
            if (!gettingNextPage) {
                getNextPage();
            }
        }

        @Produce
        public NodeIndexUpdateEvent produceNodeIndexes() {
            return new NodeIndexUpdateEvent(type, nodeIndexes);
        }

        @Produce
        public NodeIndexStatusEvent produceStatus() {
            return new NodeIndexStatusEvent(refreshing, gettingNextPage);
        }
    }
}
