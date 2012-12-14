package com.atami.mgodroid.ui.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StickyListHeadersListFragment extends BaseListFragment {

    private StickyListHeadersListView mStickyListHeadersListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        ListView lv = (ListView) layout.findViewById(android.R.id.list);
        ViewGroup parent = (ViewGroup) lv.getParent();

        //Remove ListView and add StickyListHeadersListView in its place
        int lvIndex = parent.indexOfChild(lv);
        parent.removeViewAt(lvIndex);
        mStickyListHeadersListView = onCreateStickyListHeadersListView(inflater, savedInstanceState);
        parent.addView(mStickyListHeadersListView, lvIndex, lv.getLayoutParams());

        return layout;
    }

    public final StickyListHeadersListView getPullToRefreshListView() {
        return mStickyListHeadersListView;
    }

    protected StickyListHeadersListView onCreateStickyListHeadersListView(LayoutInflater inflater,
                                                                          Bundle savedInstanceState) {
        return new StickyListHeadersListView(getActivity());
    }
}
