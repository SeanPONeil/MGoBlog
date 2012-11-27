package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.atami.mgodroid.R;
import com.atami.mgodroid.ui.base.BaseActivity;

public class NodeActivity extends BaseActivity {

    int nid;
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
    }
}
