package com.atami.mgodroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.MGoBlogConstants;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;

import static com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public class MGoBlogActivity extends BaseActivity implements MGoBlogConstants {

    // Left pane
    //ViewPager mPager;
    //TabsAdapter mAdapter;

    // Right pane
    //@InjectView(R.id.NodeFrame)
    //@Nullable
    //FrameLayout nodeFrame;

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        if (savedInstanceState == null) {
            Fragment nodeIndex = NodeIndexListFragment.newInstance(nodeIndexTitles[1], nodeIndexTypes[1]);
            Fragment nodeIndexWorker = NodeIndexListFragment.WorkerFragment.newInstance(nodeIndexTypes[1]);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(nodeIndexWorker, NodeIndexListFragment.WorkerFragment.TAG);
            ft.add(android.R.id.content, nodeIndex);
            ft.commit();
        }

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.node_index_titles,
                android.R.layout.simple_list_item_1);
        OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] titles = getResources().getStringArray(R.array.node_index_titles);
            String[] types = getResources().getStringArray(R.array.node_index_types);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                Fragment newNodeIndex = NodeIndexListFragment.newInstance(titles[position], types[position]);
                Fragment oldWorker = getSupportFragmentManager().findFragmentByTag(NodeIndexListFragment
                        .WorkerFragment.TAG);
                Fragment newWorker = NodeIndexListFragment.WorkerFragment.newInstance(types[position]);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(android.R.id.content, newNodeIndex);
                ft.remove(oldWorker);
                ft.add(newWorker, NodeIndexListFragment.WorkerFragment.TAG);
                // Apply changes
                ft.commit();
                return true;
            }
        };
        getSupportActionBar().setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().setSubtitle(R.string.app_subtitle);
        }
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
}