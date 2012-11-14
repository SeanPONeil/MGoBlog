package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;
import com.viewpagerindicator.TabPageIndicator;

public class NodeActivity extends BaseActivity {

    int nid;

    TabPageIndicator indicator;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If we are in two pane mode, finish this activity
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        setContentView(R.layout.node_pane);

        nid = getIntent().getIntExtra("nid", 0);

        indicator = (TabPageIndicator) findViewById(R.id.node_indicator);
        viewPager = (ViewPager) findViewById(R.id.NodeViewPager);

        viewPager.setAdapter(new NodeFragmentAdapter(getSupportFragmentManager(), nid));
        indicator.setViewPager(viewPager);
    }
}
