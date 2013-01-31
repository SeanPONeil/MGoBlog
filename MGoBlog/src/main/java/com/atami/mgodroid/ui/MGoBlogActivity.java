package com.atami.mgodroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;

import java.util.Arrays;

import static com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public class MGoBlogActivity extends BaseActivity implements OnNavigationListener {

    // Left pane
    //ViewPager mPager;

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane;

    private SparseArray<Fragment> nodeIndexFragments = new SparseArray<Fragment>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        SpinnerAdapter mSpinnerAdapter = new TitlesAdapter(this);
        getSupportActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);

        if (savedInstanceState == null) {
            getSupportActionBar().setSelectedNavigationItem(1);
        } else {
            getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("navigationIndex"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("navigationIndex", getSupportActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(String.valueOf(itemPosition));
        if(f == null){
            String[] types = getResources().getStringArray(R.array.node_index_types);
            Fragment newNodeIndex = NodeIndexListFragment.newInstance(types[itemPosition]);
            Fragment oldWorker = getSupportFragmentManager().findFragmentByTag(NodeIndexListFragment
                    .WorkerFragment.TAG);
            Fragment newWorker = NodeIndexListFragment.WorkerFragment.newInstance(types[itemPosition]);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, newNodeIndex, String.valueOf(itemPosition));
            if(oldWorker != null){
                ft.remove(oldWorker);
            }
            ft.add(newWorker, NodeIndexListFragment.WorkerFragment.TAG);
            // Apply changes
            ft.commit();
        }
        return true;
    }

    @Subscribe
    public void onNodeIndexItemClick(NodeIndex nodeIndex) {
        if (mIsDualPane) {
            NodeFragment nodeFragment = NodeFragment.newInstance(nodeIndex.getNid());
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.NodeFrame, nodeFragment).commit();
        } else {
            Intent intent = new Intent(this, NodeActivity.class);
            intent.putExtra("nid", nodeIndex.getNid());
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }

    private class TitlesAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

        public TitlesAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, Arrays.asList(context.getResources().getStringArray(R.array
                    .node_index_titles)));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            return convertView;
        }
    }
}