package com.atami.mgodroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.TitlePageIndicator;

public class MGoBlogActivity extends BaseActivity {

    // Left pane
    ViewPager mPager;
    NodeIndexListFragmentAdapter mAdapter;

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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setSubtitle(getResources().getString(R.string.app_subtitle));
        }
        mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
        mAdapter = new NodeIndexListFragmentAdapter(this, mPager);
        mAdapter.addTab(getSupportActionBar().newTab().setText("MGoBoard"),
                NodeIndexListFragment.class, null);
        mAdapter.addTab(getSupportActionBar().newTab().setText("MGoBlog"),
                NodeIndexListFragment.class, null);
        mAdapter.addTab(getSupportActionBar().newTab().setText("Diaries"),
                NodeIndexListFragment.class, null);
        mAdapter.addTab(getSupportActionBar().newTab().setText("mgo.licio.us"),
                NodeIndexListFragment.class, null);
        mPager.setAdapter(mAdapter);
        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
}