package com.atami.mgodroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.atami.mgodroid.MGoBlogConstants;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;

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
        final ActionBar bar = getSupportActionBar();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            bar.setSubtitle(getResources().getString(R.string.app_subtitle));
        }

        if(savedInstanceState == null){
            Fragment nodeIndex = NodeIndexListFragment.newInstance("story");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, nodeIndex);
            ft.commit();
        }

        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);
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