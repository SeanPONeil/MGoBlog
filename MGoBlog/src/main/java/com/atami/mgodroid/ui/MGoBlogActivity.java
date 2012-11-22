package com.atami.mgodroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.atami.mgodroid.ui.base.TabsAdapter;
import com.squareup.otto.Subscribe;

public class MGoBlogActivity extends BaseActivity {

    // Left pane
    ViewPager mPager;
    TabsAdapter mAdapter;

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
        final ActionBar bar = getSupportActionBar();
        final Resources res = getResources();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            bar.setSubtitle(getResources().getString(R.string.app_subtitle));
        }

        mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
        mAdapter = new TabsAdapter(this, mPager);

        Bundle args = new Bundle();
        args.putString("type", res.getString(R.string.mgoboard_type));
        mAdapter.addTab(bar.newTab().setText(R.string.mgoboard_title), NodeIndexListFragment.class, args);

        args = new Bundle();
        args.putString("type", res.getString(R.string.mgoblog_type));
        mAdapter.addTab(bar.newTab().setText(R.string.mgoblog_title), NodeIndexListFragment.class, args);

        args = new Bundle();
        args.putString("type", res.getString(R.string.diaries_type));
        mAdapter.addTab(bar.newTab().setText(R.string.diaries_title), NodeIndexListFragment.class,
                args);

        args = new Bundle();
        args.putString("type", res.getString(R.string.mgolicious_type));
        mAdapter.addTab(bar.newTab().setText(R.string.mgolicious_title), NodeIndexListFragment.class,
                args);

        mPager.setAdapter(mAdapter);
        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
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