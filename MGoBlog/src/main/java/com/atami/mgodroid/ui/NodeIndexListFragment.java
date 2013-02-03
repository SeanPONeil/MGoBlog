package com.atami.mgodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import com.activeandroid.ModelLoader;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.NodeIndexTaskStatus;
import com.atami.mgodroid.io.NodeIndexTask;
import com.atami.mgodroid.io.NodeTask;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Subscribe;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NodeIndexListFragment extends PullToRefreshListFragment
        implements OnLastItemVisibleListener, OnRefreshListener<ListView>,
        LoaderManager.LoaderCallbacks<List<NodeIndex>> {

    private final static String TAG = "NodeIndexListFragment";

    //DB and API parameters
    private String column;
    private String value;

    private NodeIndexAdapter mAdapter;

    @Inject
    TaskQueue<NodeIndexTask> queue;

    @Inject
    TaskQueue<NodeTask> nodeQueue;

    /**
     * Displays a list of node indexes from MGoBlog.
     *
     * @param column The column to query the local database on, as well as the web service
     * @param value  Value for selected column
     * @return NodeIndexListFragment
     */
    public static NodeIndexListFragment newInstance(String column, String value) {
        NodeIndexListFragment f = new NodeIndexListFragment();

        Bundle args = new Bundle();
        args.putString("column", column);
        args.putString("value", value);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        column = getArguments().getString("column");
        value = getArguments().getString("value");

        setHasOptionsMenu(true);

        queue.add(new NodeIndexTask(column, value, 0, getTag()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        mAdapter = new NodeIndexAdapter(getActivity(), R.layout.list_item_two_line, new ArrayList<NodeIndex>());
        setListAdapter(mAdapter);

        getPullToRefreshListView().setOnLastItemVisibleListener(this);
        getPullToRefreshListView().setOnRefreshListener(this);

        //Bug in ActiveAndroid ModelLoader: Cached results in LoaderManager aren't updated
        //when the ModelLoaders From changes
        if(savedInstanceState == null){
            getActivity().getSupportLoaderManager().restartLoader(0, null, this);
        }else{
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Intent intent = new Intent(this, NodeActivity.class);
        //intent.putExtra("nid", mAdapter.getItem(position - 1).getNid());
        //startActivity(intent);
        nodeQueue.add(new NodeTask(mAdapter.getItem(position - 1).getNid(), getTag()));
    }

    @Override
    public void onLastItemVisible() {
        queue.add(new NodeIndexTask(column, value, mAdapter.getCount() / 20, getTag()));
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        getPullToRefreshListView().setLastUpdatedLabel(
                DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));
        queue.add(new NodeIndexTask(column, value, 0, getTag()));
    }

    @Subscribe
    public void onNewNodeIndexTaskStatus(NodeIndexTaskStatus status) {
        if (status.tag.equals(getTag())) {
            if (status.running) {
                getPullToRefreshListView().setRefreshing();
            } else {
                getPullToRefreshListView().onRefreshComplete();
            }
        }
    }

    @Override
    public Loader<List<NodeIndex>> onCreateLoader(int id, Bundle args) {
        String whereArgs = new StringBuilder().append(column).append(" = \"").append(value).append("\"").toString();
        From query = new Select().from(NodeIndex.class).where(whereArgs).orderBy("created DESC");
        return new ModelLoader<NodeIndex>(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<List<NodeIndex>> nodeIndexLoader, List<NodeIndex> nodeIndexes) {
        mAdapter.setNodeIndexes(nodeIndexes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<NodeIndex>> nodeIndexLoader) {
        mAdapter.setNodeIndexes(null);
    }
}
