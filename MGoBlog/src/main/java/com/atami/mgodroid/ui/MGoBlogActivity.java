package com.atami.mgodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
    TitlePageIndicator mIndicator;

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
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_subtitle));

        mPager = (ViewPager) findViewById(R.id.NodeIndexViewPager);
        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);

        mPager.setAdapter(new NodeIndexListFragmentAdapter(getResources(), getSupportFragmentManager()));
        mIndicator.setViewPager(mPager);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.mgoblog, menu);
        return true;
    }
}