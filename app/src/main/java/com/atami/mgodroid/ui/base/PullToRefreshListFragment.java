/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.atami.mgodroid.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A sample implementation of how to the PullToRefreshListView with
 * ListFragment. This implementation simply replaces the ListView that
 * ListFragment creates with a new PullToRefreshListView. This means that
 * ListFragment still works 100% (e.g. <code>setListShown(...)</code>).
 * <p/>
 * The new PullToRefreshListView is created in the method
 * <code>onCreatePullToRefreshListView()</code>. If you wish to customise the
 * PullToRefreshListView then override this method and return your customised
 * instance.
 *
 * @author Chris Banes
 * @author Sean O'Neil
 */
public class PullToRefreshListFragment extends BaseListFragment {

    private PullToRefreshListView mPullToRefreshListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        ListView lv = (ListView) layout.findViewById(android.R.id.list);
        View ev = lv.getEmptyView();
        ViewGroup parent = (ViewGroup) lv.getParent();

        //Remove ListView and add PullToRefreshListView in its place
        int lvIndex = parent.indexOfChild(lv);
        parent.removeViewAt(lvIndex);
        mPullToRefreshListView = onCreatePullToRefreshListView();
        mPullToRefreshListView.getRefreshableView().setEmptyView(ev);
        parent.addView(mPullToRefreshListView, lvIndex, lv.getLayoutParams());

        return layout;
    }

    /**
     * @return The {@link PullToRefreshListView} attached to this ListFragment.
     */
    public final PullToRefreshListView getPullToRefreshListView() {
        return mPullToRefreshListView;
    }

    public final void setPullToRefreshListView(PullToRefreshListView lv){
        mPullToRefreshListView = lv;
    }

    protected PullToRefreshListView onCreatePullToRefreshListView() {
        return new PullToRefreshListView(getActivity());
    }

}