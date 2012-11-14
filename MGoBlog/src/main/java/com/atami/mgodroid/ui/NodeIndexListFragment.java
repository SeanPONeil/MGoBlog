package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.APIModule.MGoBlogAPI;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.github.kevinsawicki.wishlist.AsyncLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NodeIndexListFragment extends PullToRefreshListFragment
        implements OnLastItemVisibleListener, OnRefreshListener<ListView>,
        LoaderManager.LoaderCallbacks<List<NodeIndex>> {

    @Inject
    MGoBlogAPI api;

    //Type parameter used in API call
    private String typeParam;

    // This is the Adapter being used to display the list's data.
    NodeIndexAdapter mAdapter;

    //Constants differentiating different loading types
    private static final int REFRESH = 1;
    private static final int NEXT_PAGE = 2;

    public static NodeIndexListFragment newInstance(String type) {
        NodeIndexListFragment f = new NodeIndexListFragment();

        Bundle args = new Bundle();
        args.putString("type", type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeParam = getArguments().getString("type");
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
    public void onSaveInstanceState(Bundle outState) {
        if (mAdapter != null) {
            String json = new Gson().toJson(mAdapter.getNodeIndexes());
            outState.putString("data", json);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPullToRefreshListView().getRefreshableView().setDivider(getResources().getDrawable(R.drawable.list_divider));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("data");
            Type type = new TypeToken<List<NodeIndex>>() {
            }.getType();
            List<NodeIndex> nodeIndexes = new Gson().fromJson(json, type);
            mAdapter = new NodeIndexAdapter(getActivity(), android.R.layout.simple_list_item_2, nodeIndexes);
        } else {
            mAdapter = new NodeIndexAdapter(getActivity(), android.R.layout.simple_list_item_2,
                    new ArrayList<NodeIndex>());
        }

        setListAdapter(mAdapter);

        getPullToRefreshListView().setOnLastItemVisibleListener(this);
        getPullToRefreshListView().setOnRefreshListener(this);

        getLoaderManager().initLoader(REFRESH, null, this);
        if (getLoaderManager().hasRunningLoaders()) {
            getPullToRefreshListView().setRefreshing();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        bus.post(mAdapter.getItem(position));
    }

    @Override
    public void onLastItemVisible() {
        if (!getLoaderManager().hasRunningLoaders()) {
            getLoaderManager().restartLoader(NEXT_PAGE, null, this);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        getPullToRefreshListView().setLastUpdatedLabel(
                DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));
        getLoaderManager().restartLoader(REFRESH, null, this);
    }

    @Override
    public Loader<List<NodeIndex>> onCreateLoader(final int id, Bundle bundle) {
        return new AsyncLoader<List<NodeIndex>>(getActivity()) {
            @Override
            public List<NodeIndex> loadInBackground() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                switch (id) {
                    case REFRESH:
                        return api.getNodeIndex(typeParam, "0", "0");
                    case NEXT_PAGE:
                        return api.getNodeIndex(typeParam, String.valueOf(mAdapter.getNodeIndexes().size() / 20), "0");
                    default:
                        return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<NodeIndex>> loader, List<NodeIndex> nodeIndexes) {
        switch (loader.getId()) {
            case REFRESH:
                mAdapter.setNodeIndexes(nodeIndexes);
                break;
            case NEXT_PAGE:
                List<NodeIndex> currentList = mAdapter.getNodeIndexes();
                currentList.addAll(nodeIndexes);
                mAdapter.setNodeIndexes(currentList);
        }
        mAdapter.notifyDataSetChanged();
        getPullToRefreshListView().onRefreshComplete();
    }

    @Override
    public void onLoaderReset(Loader<List<NodeIndex>> loader) {
        mAdapter.getNodeIndexes().clear();
    }
}
